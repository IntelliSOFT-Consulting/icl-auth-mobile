package icl.ohs.libs.auth.profile

data class ProfileUiState(
    // Personal Details
    val firstName: String = "",
    val lastName: String = "",
    val idNumber: String = "",
    val role: String = "",
    val status: String = "Active",

    // Contact Information
    val phone: String = "",
    val email: String = "",

    // Location Information
    val isSupervisor: Boolean = false,
    val locationInfo: LocationInfo = LocationInfo(),

    // Community Health Units
    val communityHealthUnits: List<String> = emptyList()
) {
    val fullName: String get() = if (firstName.isBlank() && lastName.isBlank()) "" else "$firstName $lastName".trim()
    val initials: String get() {
        val f = if (firstName.isNotEmpty()) firstName.take(1) else ""
        val l = if (lastName.isNotEmpty()) lastName.take(1) else ""
        return (f + l).uppercase()
    }
}

data class LocationInfo(
    val facility: String = "",
    val facilityName: String = "",
    val ward: String = "",
    val wardName: String = "",
    val subCounty: String = "",
    val subCountyName: String = "",
    val county: String = "",
    val countyName: String = "",
    val country: String = "",
    val countryName: String = ""
)
