package icl.ohs.libs.auth.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ProfileViewModel {
    var uiState by mutableStateOf(ProfileRepository.getProfile())
        private set

    var isRefreshing by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Own scope tied to this ViewModel's lifetime instead of a fresh, uncancellable
    // scope per call - cancel() must be invoked (e.g. from a DisposableEffect) when
    // the screen leaves composition so an in-flight refresh doesn't leak.
    private val viewModelScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun refresh() {
        if (isRefreshing) return

        isRefreshing = true
        errorMessage = null
        viewModelScope.launch {
            val result = ProfileRepository.refreshProfile()
            result.fold(
                onSuccess = { uiState = it },
                onFailure = { throwable ->
                    // ProfileRepository/IclAuth surface a network-error message here when
                    // the /provider/me call fails, including when there's no connectivity.
                    errorMessage = throwable.message
                        ?: "Unable to refresh profile. Check your internet connection and try again."
                },
            )
            isRefreshing = false
        }
    }

    fun dismissError() {
        errorMessage = null
    }

    fun clear() {
        viewModelScope.cancel()
    }
}
