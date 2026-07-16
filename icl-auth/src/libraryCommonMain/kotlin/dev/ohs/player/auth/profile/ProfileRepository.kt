package dev.ohs.player.auth.profile

object ProfileRepository {
    private var cachedProfile: ProfileUiState? = null

    fun getProfile(): ProfileUiState {
        return cachedProfile ?: ProfileUiState()
    }

    fun saveProfile(profile: ProfileUiState): Boolean {
        cachedProfile = profile
        // In the future, this will also call an API
        return true
    }
}
