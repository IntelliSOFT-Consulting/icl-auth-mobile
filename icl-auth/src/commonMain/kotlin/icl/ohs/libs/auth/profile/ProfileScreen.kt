package icl.ohs.libs.auth.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onBack: () -> Unit
) {
    val uiState = viewModel.uiState
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }, enabled = !viewModel.isRefreshing) {
                        if (viewModel.isRefreshing) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                        }
                    }
                    IconButton(onClick = { /* Menu */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1B1B1F),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF1B1B1F) // Dark background like the photo
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Main Profile Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF252529))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF3B2F2F)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = uiState.initials, color = Color(0xFFE57373), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = uiState.fullName, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text(text = "ID Number: ${uiState.idNumber.ifBlank { "Not provided" }}", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Role: ${uiState.role}", color = Color.White)
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF1B5E20).copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = uiState.status,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                color = Color(0xFF66BB6A),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Contact Section
            Text(text = "Contact", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF252529))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    DetailRow(icon = Icons.Default.Phone, label = "Phone", value = uiState.phone)
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    DetailRow(icon = Icons.Default.Email, label = "Email Address", value = uiState.email)
                }
            }

            // Location / Supervisor Area Section
            val locationTitle = if (uiState.isSupervisor) "Supervisor Area" else "Location Information"
            Text(text = locationTitle, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF252529))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    LocationField(label = "Country", value = uiState.locationInfo.countryName)
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    LocationField(label = "County", value = uiState.locationInfo.countyName)
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    LocationField(label = "Sub-county", value = uiState.locationInfo.subCountyName)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(text = "Community Health Unit(s) (CHUs)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    uiState.communityHealthUnits.forEachIndexed { index, unit ->
                        Text(text = "${index + 1}. $unit", color = Color.LightGray, fontSize = 14.sp, modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun DetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.Gray)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = value, color = Color.Gray, fontSize = 13.sp)
        }
    }
}

@Composable
fun LocationField(label: String, value: String) {
    Column {
        Text(text = label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Text(text = value, color = Color.Gray, fontSize = 14.sp)
    }
}
