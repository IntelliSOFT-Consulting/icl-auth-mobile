package dev.ohs.player.auth.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun ProfileRoute(
    onBack: () -> Unit,
    onProfileSaved: () -> Unit = {}
) {
    val viewModel = remember { ProfileViewModel() }

    ProfileScreen(
        viewModel = viewModel,
        onBack = onBack
    )
    
    // Optional: side effect to call onProfileSaved when profileSaved becomes true
    if (viewModel.profileSaved) {
        // You might want to delay this or trigger it only once
        // onProfileSaved()
    }
}
