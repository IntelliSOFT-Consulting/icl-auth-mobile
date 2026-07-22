package icl.ohs.libs.auth.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class ProfileViewModel {
    var uiState by mutableStateOf(ProfileRepository.getProfile())
        private set

    var isRefreshing by mutableStateOf(false)
        private set

    fun refresh() {
        if (isRefreshing) return
        
        isRefreshing = true
        CoroutineScope(Dispatchers.Main).launch {
            val result = ProfileRepository.refreshProfile()
            if (result.isSuccess) {
                uiState = result.getOrThrow()
            }
            isRefreshing = false
        }
    }
}
