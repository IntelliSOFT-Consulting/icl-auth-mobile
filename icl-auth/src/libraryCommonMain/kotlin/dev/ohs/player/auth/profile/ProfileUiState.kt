package dev.ohs.player.auth.profile

data class ProfileUiState(

    // ================= PERSONAL INFORMATION =================

    val firstName: String = "",
    val lastName: String = "",
    val fullName: String = "",
    val gender: String = "",
    val dateOfBirth: String = "",

    // ================= CONTACT INFORMATION =================

    val email: String = "",
    val phone: String = "",

    // ================= EMPLOYMENT INFORMATION =================

    val idNumber: String = "",
    val role: String = "",
    val practitionerRole: String = "",

    // ================= LOCATION INFORMATION =================

    val facility: String = "",
    val ward: String = "",
    val subCounty: String = "",
    val county: String = "",
    val country: String = ""
)