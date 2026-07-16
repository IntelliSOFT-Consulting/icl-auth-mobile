package dev.ohs.player.auth.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProfileRepository = ProfileRepository
) {

    var uiState by mutableStateOf(repository.getProfile())
        private set

    var profileSaved by mutableStateOf(false)
        private set
        
    var validationMessage by mutableStateOf<String?>(null)
        private set

    // ================= PERSONAL INFORMATION =================

    fun updateFirstName(value: String) {
        uiState = uiState.copy(
            firstName = value,
            fullName = listOf(value, uiState.lastName)
                .filter { it.isNotBlank() }
                .joinToString(" ")
        )
        profileSaved = false
    }

    fun updateLastName(value: String) {
        uiState = uiState.copy(
            lastName = value,
            fullName = listOf(uiState.firstName, value)
                .filter { it.isNotBlank() }
                .joinToString(" ")
        )
        profileSaved = false
    }

    fun updateGender(value: String) {
        uiState = uiState.copy(gender = value)
        profileSaved = false
    }

    fun updateDateOfBirth(value: String) {
        uiState = uiState.copy(dateOfBirth = value)
        profileSaved = false
    }

    // ================= CONTACT INFORMATION =================

    fun updateEmail(value: String) {
        uiState = uiState.copy(email = value)
        profileSaved = false
    }

    fun updatePhone(value: String) {
        uiState = uiState.copy(phone = value)
        profileSaved = false
    }

    // ================= EMPLOYMENT INFORMATION =================

    fun updateIdNumber(value: String) {
        uiState = uiState.copy(idNumber = value)
        profileSaved = false
    }

    fun updateRole(value: String) {
        uiState = uiState.copy(role = value)
        profileSaved = false
    }

    fun updatePractitionerRole(value: String) {
        uiState = uiState.copy(practitionerRole = value)
        profileSaved = false
    }

    // ================= LOCATION INFORMATION =================

    fun updateFacility(value: String) {
        uiState = uiState.copy(facility = value)
        profileSaved = false
    }

    fun updateWard(value: String) {
        uiState = uiState.copy(ward = value)
        profileSaved = false
    }

    fun updateSubCounty(value: String) {
        uiState = uiState.copy(subCounty = value)
        profileSaved = false
    }

    fun updateCounty(value: String) {
        uiState = uiState.copy(county = value)
        profileSaved = false
    }

    fun updateCountry(value: String) {
        uiState = uiState.copy(country = value)
        profileSaved = false
    }

    // ================= SAVE PROFILE =================

    fun saveProfile() {
        if (
            uiState.firstName.isBlank() ||
            uiState.lastName.isBlank() ||
            uiState.gender.isBlank() ||
            uiState.dateOfBirth.isBlank() ||
            uiState.email.isBlank() ||
            uiState.phone.isBlank()
        ) {
            validationMessage = "Please complete your Personal Information and Contact Information before saving."
            return
        }

        validationMessage = null
        
        // Save to repository (singleton)
        repository.saveProfile(uiState)
        
        profileSaved = true

        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            profileSaved = false
        }
    }
}
