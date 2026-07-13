/*
 * Copyright 2026 Open Health Stack Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package icl.ohs.libs.auth

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class ForgotPasswordClientTest {

  @Test
  fun resolveForgotPasswordConfig_usesAuthBaseUrlAndDefaultEndpoint() {
    val config =
      resolveForgotPasswordConfig(
        screenConfig = ForgotPasswordScreenConfig(),
        authConfig = IclAuthConfig(baseAuthUrl = "https://auth.example.com"),
      )

    assertEquals("https://auth.example.com/provider/reset-password", config.resetPasswordUrl)
  }

  @Test
  fun validateForgotPasswordRequest_rejectsBlankIdNumber() {
    val config =
      resolveForgotPasswordConfig(
        screenConfig = ForgotPasswordScreenConfig(),
        authConfig = IclAuthConfig(baseAuthUrl = "https://auth.example.com"),
      )

    val failure =
      validateForgotPasswordRequest(
        config = config,
        request = ForgotPasswordReq(idNumber = "", email = "user@example.com"),
      )

    assertEquals(config.messages.missingIdNumber, failure?.message)
  }

  @Test
  fun normalizeForgotPasswordEmail_decodesExistingQueryEncodingOnce() {
    assertEquals(
      "jkiprotich@intellisoftkenya.com",
      normalizeForgotPasswordEmail("jkiprotich%40intellisoftkenya.com"),
    )
    assertEquals(
      "jkiprotich+test@intellisoftkenya.com",
      normalizeForgotPasswordEmail("jkiprotich%2Btest%40intellisoftkenya.com"),
    )
  }

  @Test
  fun forgotPassword_returnsSuccessForSuccessfulResponses() = runTest {
    val config =
      resolveForgotPasswordConfig(
        screenConfig = ForgotPasswordScreenConfig(),
        authConfig = IclAuthConfig(baseAuthUrl = "https://auth.example.com"),
      )
    val client =
      HttpClient(
        MockEngine { request ->
          when {
            request.method == HttpMethod.Get &&
              request.url.encodedPath == "/provider/reset-password" &&
              request.url.parameters["idNumber"] == "32645167" &&
              request.url.parameters["email"] == "jkiprotich@intellisoftkenya.com" &&
              request.url.toString().contains("email=jkiprotich%40intellisoftkenya.com") ->
              respond(
                content =
                  """{"status":"success","response":"Check your email for the password reset code sent."}""",
                status = HttpStatusCode.OK,
                headers =
                  headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
              )

            else -> error("Unexpected request: ${request.method.value} ${request.url}")
          }
        }
      ) {
        expectSuccess = false
      }
    val service = LoginService(client)

    try {
      val result =
        service.forgotPassword(
          config = config,
          request =
            ForgotPasswordReq(idNumber = "32645167", email = "jkiprotich@intellisoftkenya.com"),
        )

      val success = assertIs<ForgotPasswordAttemptResult.Success>(result)
      assertEquals(200, success.value.statusCode)
      assertEquals("Check your email for the password reset code sent.", success.value.message)
    } finally {
      client.close()
    }
  }

  @Test
  fun forgotPassword_avoidsDoubleEncodingWhenEmailIsAlreadyEncoded() = runTest {
    val config =
      resolveForgotPasswordConfig(
        screenConfig = ForgotPasswordScreenConfig(),
        authConfig = IclAuthConfig(baseAuthUrl = "https://auth.example.com"),
      )
    val client =
      HttpClient(
        MockEngine { request ->
          val requestUrl = request.url.toString()
          assertTrue(requestUrl.contains("email=jkiprotich%40intellisoftkenya.com"))
          assertFalse(requestUrl.contains("%2540"))
          assertEquals("jkiprotich@intellisoftkenya.com", request.url.parameters["email"])

          respond(
            content =
              """{"status":"success","response":"Check your email for the password reset code sent."}""",
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
          )
        }
      ) {
        expectSuccess = false
      }
    val service = LoginService(client)

    try {
      val result =
        service.forgotPassword(
          config = config,
          request =
            ForgotPasswordReq(idNumber = "32645167", email = "jkiprotich%40intellisoftkenya.com"),
        )

      val success = assertIs<ForgotPasswordAttemptResult.Success>(result)
      assertEquals("Check your email for the password reset code sent.", success.value.message)
    } finally {
      client.close()
    }
  }

  @Test
  fun forgotPassword_returnsFailureWhenPayloadStatusIsNotSuccess() = runTest {
    val config =
      resolveForgotPasswordConfig(
        screenConfig = ForgotPasswordScreenConfig(),
        authConfig = IclAuthConfig(baseAuthUrl = "https://auth.example.com"),
      )
    val client =
      HttpClient(
        MockEngine {
          respond(
            content = """{"status":"error","response":"Provider account was not found."}""",
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
          )
        }
      ) {
        expectSuccess = false
      }
    val service = LoginService(client)

    try {
      val result =
        service.forgotPassword(
          config = config,
          request = ForgotPasswordReq(idNumber = "32645167", email = "user@example.com"),
        )

      val failure = assertIs<ForgotPasswordAttemptResult.Failure>(result)
      assertEquals("Provider account was not found.", failure.value.message)
      assertEquals(200, failure.value.statusCode)
    } finally {
      client.close()
    }
  }
}
