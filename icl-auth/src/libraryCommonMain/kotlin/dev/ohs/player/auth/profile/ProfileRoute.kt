package dev.ohs.player.auth.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun ProfileRoute(
    onProfileSaved: () -> Unit = {}
) {

    val viewModel = remember { ProfileViewModel() }

    val uiState = viewModel.uiState

    ProfileScreen(

        uiState = uiState,

        onFirstNameChanged = viewModel::updateFirstName,

        onLastNameChanged = viewModel::updateLastName,

        onGenderChanged = viewModel::updateGender,

        onDateOfBirthChanged = viewModel::updateDateOfBirth,

        onEmailChanged = viewModel::updateEmail,

        onPhoneChanged = viewModel::updatePhone,

        onIdNumberChanged = viewModel::updateIdNumber,

        onRoleChanged = viewModel::updateRole,

        onPractitionerRoleChanged = viewModel::updatePractitionerRole,

        onFacilityChanged = viewModel::updateFacility,

        onWardChanged = viewModel::updateWard,

        onSubCountyChanged = viewModel::updateSubCounty,

        onCountyChanged = viewModel::updateCounty,

        onCountryChanged = viewModel::updateCountry,

        profileSaved = viewModel.profileSaved,

        validationMessage = viewModel.validationMessage,

        onSave = {
            viewModel.saveProfile()
        }
    )
}