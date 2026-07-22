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

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.savedstate.read
import dev.ohs.player.library.registry.LocalViewRegistry
import dev.ohs.player.reference.app.feature.group.list.GroupListScreen
import dev.ohs.player.reference.app.feature.group.profile.GroupProfileScreen
import dev.ohs.player.reference.app.feature.patient.profile.PatientProfileScreen
import icl.ohs.libs.auth.IclAuth
import icl.ohs.libs.auth.IclAuthConfig
import icl.ohs.libs.auth.profile.ProfileScreen
import icl.ohs.libs.auth.profile.ProfileViewModel

private const val PROFILE_ROUTE = "profile"
private const val GROUP_LIST_ROUTE = "groupList"
private const val GROUP_PROFILE_ROUTE = "groupProfile"
private const val PATIENT_PROFILE_ROUTE = "patientProfile"
private const val GROUP_ID_ARG = "groupId"
private const val PATIENT_ID_ARG = "patientId"

private val AUTH_CONFIG =
  IclAuthConfig(baseAuthUrl = "https://dsrkeycloak.intellisoftkenya.com/auth")

@Composable
fun App() {
  remember(AUTH_CONFIG) { IclAuth.initialize(AUTH_CONFIG) }
  val registry = remember { buildAppViewRegistry() }

  CompositionLocalProvider(LocalViewRegistry provides registry) {
    OhsPlayerTheme {
      var isLoggedIn by rememberSaveable { mutableStateOf(IclAuth.hasValidAccessToken()) }

      if (isLoggedIn) {
        ReferenceAppNavigation(onLogout = { isLoggedIn = false })
      } else {
        AuthNavigation(onAuthenticated = { isLoggedIn = true })
      }
    }
  }
}

@Composable
private fun ReferenceAppNavigation(onLogout: () -> Unit) {
  val navController = rememberNavController()

  NavHost(navController = navController, startDestination = GROUP_LIST_ROUTE) {

    // New Profile Screen
    composable(PROFILE_ROUTE) {
      val viewModel = remember { ProfileViewModel() }
      ProfileScreen(
        viewModel = viewModel,
        onBack = { navController.popBackStack() }
      )
    }

    // Household list
    composable(GROUP_LIST_ROUTE) {
      GroupListScreen(
        onProfileClick = { navController.navigate(PROFILE_ROUTE) },
        onSettingsClick = { /* Handle settings */ },
        onLogoutClick = { onLogout() },
        onGroupClick = { id -> navController.navigate("$GROUP_PROFILE_ROUTE/$id") }
      )
    }

    // Household profile (head + members)
    composable(
      route = "$GROUP_PROFILE_ROUTE/{$GROUP_ID_ARG}",
      arguments = listOf(navArgument(GROUP_ID_ARG) { type = NavType.StringType }),
    ) { back ->
      val groupId = back.arguments?.read { getString(GROUP_ID_ARG) }.orEmpty()
      GroupProfileScreen(
        groupId = groupId,
        onBack = { navController.popBackStack() },
        onMemberClick = { id -> navController.navigate("$PATIENT_PROFILE_ROUTE/$id") },
      )
    }

    // Patient IPS summary
    composable(
      route = "$PATIENT_PROFILE_ROUTE/{$PATIENT_ID_ARG}",
      arguments = listOf(navArgument(PATIENT_ID_ARG) { type = NavType.StringType }),
    ) { back ->
      val patientId = back.arguments?.read { getString(PATIENT_ID_ARG) }.orEmpty()
      PatientProfileScreen(patientId = patientId, onBack = { navController.popBackStack() })
    }
  }
}
