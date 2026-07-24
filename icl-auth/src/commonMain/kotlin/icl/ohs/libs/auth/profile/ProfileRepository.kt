package icl.ohs.libs.auth.profile

import icl.ohs.libs.auth.IclAuth

/**
 * Singleton repository to persist profile data during the app session.
 * On first access, pre-fills from the provider profile already fetched at login.
 */
object ProfileRepository {
    private var cachedProfile: ProfileUiState? = null

    fun getProfile(): ProfileUiState {
        // If we already have a cached profile, return it
        if (cachedProfile != null) return cachedProfile!!

        // Otherwise, pre-fill from current auth state
        return updateFromAuth()
    }

    suspend fun refreshProfile(): Result<ProfileUiState> {
        val result = IclAuth.refreshProviderProfile()
        return result.map { updateFromAuth() }
    }

    private fun updateFromAuth(): ProfileUiState {
        val providerUser = IclAuth.currentProviderUser()
        val newState = if (providerUser != null) {
            ProfileUiState(
                firstName = providerUser.firstName.orEmpty(),
                lastName = providerUser.lastName.orEmpty(),
                idNumber = providerUser.idNumber.orEmpty(),
                role = providerUser.role.orEmpty(),
                status = if (providerUser.status == false) "Inactive" else "Active",
                phone = providerUser.phone.orEmpty(),
                email = providerUser.email.orEmpty(),
                locationInfo = providerUser.locationInfo?.let {
                    LocationInfo(
                        facilityName = it.facilityName.orEmpty(),
                        wardName = it.wardName.orEmpty(),
                        subCountyName = it.subCountyName.orEmpty(),
                        countyName = it.countyName.orEmpty(),
                        countryName = it.countryName.orEmpty(),
                    )
                } ?: LocationInfo(),
                communityHealthUnits = providerUser.communityHealthUnits.orEmpty()
            )
        } else {
            ProfileUiState()
        }
        cachedProfile = newState
        return newState
    }

    fun clearProfile() {
        cachedProfile = null
    }
}
