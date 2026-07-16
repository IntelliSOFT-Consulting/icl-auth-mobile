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
package dev.ohs.player.reference.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import dev.ohs.player.auth.profile.ProfileRoute
import dev.ohs.player.library.registry.LocalViewRegistry
import icl.ohs.libs.auth.IclAuth
import icl.ohs.libs.auth.IclAuthConfig

private val AUTH_CONFIG =
  IclAuthConfig(baseAuthUrl = "https://dsrkeycloak.intellisoftkenya.com/auth")

@Composable
fun App() {
  remember(AUTH_CONFIG) { IclAuth.initialize(AUTH_CONFIG) }
  val registry = remember { buildAppViewRegistry() }

  CompositionLocalProvider(LocalViewRegistry provides registry) {
    OhsPlayerTheme {
      // Showing Profile Screen directly as requested to work on it in isolation.
      ProfileRoute(onBack = { })
    }
  }
}
