package dev.ohs.player.auth.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(

    uiState: ProfileUiState,

    onFirstNameChanged: (String) -> Unit,

    onLastNameChanged: (String) -> Unit,

    onGenderChanged: (String) -> Unit,

    onDateOfBirthChanged: (String) -> Unit,

    onEmailChanged: (String) -> Unit,

    onPhoneChanged: (String) -> Unit,

    onIdNumberChanged: (String) -> Unit,

    onRoleChanged: (String) -> Unit,

    onPractitionerRoleChanged: (String) -> Unit,

    onFacilityChanged: (String) -> Unit,

    onWardChanged: (String) -> Unit,

    onSubCountyChanged: (String) -> Unit,

    onCountyChanged: (String) -> Unit,

    onCountryChanged: (String) -> Unit,

    onSave: () -> Unit,

    profileSaved: Boolean = false,

    validationMessage: String? = null
) {

    val profile = uiState

    var personalExpanded by remember { mutableStateOf(true) }
    var contactExpanded by remember { mutableStateOf(false) }
    var employmentExpanded by remember { mutableStateOf(false) }
    var locationExpanded by remember { mutableStateOf(false) }

    var genderExpanded by remember { mutableStateOf(false) }

    val initials = buildString {

        if (profile.firstName.isNotBlank()) {
            append(profile.firstName.first().uppercase())
        }

        if (profile.lastName.isNotBlank()) {
            append(profile.lastName.first().uppercase())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        CircleShape
                    ),

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = initials,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }

        Text(
            text = "My Profile",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.headlineMedium
        )
        // =====================================================
// PERSONAL INFORMATION
// =====================================================

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {

            Column {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            personalExpanded = !personalExpanded
                        }
                        .padding(16.dp),

                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Personal Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = if (personalExpanded) "▲" else "▼"
                    )
                }

                if (personalExpanded) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        // First Name & Last Name
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            OutlinedTextField(
                                value = profile.firstName,
                                onValueChange = onFirstNameChanged,
                                modifier = Modifier.weight(1f),
                                label = {
                                    Text("First Name")
                                }
                            )

                            OutlinedTextField(
                                value = profile.lastName,
                                onValueChange = onLastNameChanged,
                                modifier = Modifier.weight(1f),
                                label = {
                                    Text("Last Name")
                                }
                            )
                        }

                        // Gender & Date of Birth
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            Box(
                                modifier = Modifier.weight(1f)
                            ) {

                                OutlinedTextField(
                                    value = profile.gender,
                                    onValueChange = {},
                                    readOnly = true,
                                    enabled = false,
                                    modifier = Modifier.fillMaxWidth(),
                                    label = {
                                        Text("Gender")
                                    }
                                )

                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .clickable {
                                            genderExpanded = true
                                        }
                                ) {
                                }

                                DropdownMenu(
                                    expanded = genderExpanded,
                                    onDismissRequest = {
                                        genderExpanded = false
                                    }
                                ) {

                                    DropdownMenuItem(
                                        text = {
                                            Text("Male")
                                        },
                                        onClick = {
                                            onGenderChanged("Male")
                                            genderExpanded = false
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = {
                                            Text("Female")
                                        },
                                        onClick = {
                                            onGenderChanged("Female")
                                            genderExpanded = false
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = {
                                            Text("Other")
                                        },
                                        onClick = {
                                            onGenderChanged("Other")
                                            genderExpanded = false
                                        }
                                    )
                                }
                            }

                            OutlinedTextField(
                                value = profile.dateOfBirth,
                                onValueChange = onDateOfBirthChanged,
                                modifier = Modifier.weight(1f),
                                label = {
                                    Text("Date of Birth")
                                },
                                placeholder = {
                                    Text("dd/mm/yyyy")
                                }
                            )
                        }

                        // Full Name
                        OutlinedTextField(
                            value = profile.fullName,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text("Full Name")
                            }
                        )
                    }
                }
            }
        }
                                    // =====================================================
                                    // CONTACT INFORMATION
                                    // =====================================================

                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                    ) {

                                        Column {

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        contactExpanded = !contactExpanded
                                                    }
                                                    .padding(16.dp),

                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {

                                                Text(
                                                    text = "Contact Information",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.Bold
                                                )

                                                Text(
                                                    text = if (contactExpanded) "▲" else "▼"
                                                )
                                            }

                                            if (contactExpanded) {

                                                Column(
                                                    modifier = Modifier.padding(16.dp),
                                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                                ) {

                                                    OutlinedTextField(
                                                        value = profile.email,
                                                        onValueChange = onEmailChanged,
                                                        modifier = Modifier.fillMaxWidth(),
                                                        label = {
                                                            Text("Email Address")
                                                        }
                                                    )

                                                    OutlinedTextField(
                                                        value = profile.phone,
                                                        onValueChange = onPhoneChanged,
                                                        modifier = Modifier.fillMaxWidth(),
                                                        label = {
                                                            Text("Phone Number")
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    // =====================================================
                                    // EMPLOYMENT INFORMATION
                                    // =====================================================

                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                    ) {

                                        Column {

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        employmentExpanded = !employmentExpanded
                                                    }
                                                    .padding(16.dp),

                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {

                                                Text(
                                                    text = "Employment Information",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.Bold
                                                )

                                                Text(
                                                    text = if (employmentExpanded) "▲" else "▼"
                                                )
                                            }

                                            if (employmentExpanded) {

                                                Column(
                                                    modifier = Modifier.padding(16.dp),
                                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                                ) {

                                                    OutlinedTextField(
                                                        value = profile.idNumber,
                                                        onValueChange = onIdNumberChanged,
                                                        modifier = Modifier.fillMaxWidth(),
                                                        label = {
                                                            Text("ID Number")
                                                        }
                                                    )

                                                    OutlinedTextField(
                                                        value = profile.role,
                                                        onValueChange = onRoleChanged,
                                                        modifier = Modifier.fillMaxWidth(),
                                                        label = {
                                                            Text("Role")
                                                        }
                                                    )

                                                    OutlinedTextField(
                                                        value = profile.practitionerRole,
                                                        onValueChange = onPractitionerRoleChanged,
                                                        modifier = Modifier.fillMaxWidth(),
                                                        label = {
                                                            Text("Practitioner Role")
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    // =====================================================
                                    // LOCATION INFORMATION
                                    // =====================================================

                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                    ) {

                                        Column {

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        locationExpanded = !locationExpanded
                                                    }
                                                    .padding(16.dp),

                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {

                                                Text(
                                                    text = "Location Information",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.Bold
                                                )

                                                Text(
                                                    text = if (locationExpanded) "▲" else "▼"
                                                )
                                            }

                                            if (locationExpanded) {

                                                Column(
                                                    modifier = Modifier.padding(16.dp),
                                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                                ) {

                                                    OutlinedTextField(
                                                        value = profile.facility,
                                                        onValueChange = onFacilityChanged,
                                                        modifier = Modifier.fillMaxWidth(),
                                                        label = {
                                                            Text("Facility")
                                                        }
                                                    )

                                                    OutlinedTextField(
                                                        value = profile.ward,
                                                        onValueChange = onWardChanged,
                                                        modifier = Modifier.fillMaxWidth(),
                                                        label = {
                                                            Text("Ward")
                                                        }
                                                    )

                                                    OutlinedTextField(
                                                        value = profile.subCounty,
                                                        onValueChange = onSubCountyChanged,
                                                        modifier = Modifier.fillMaxWidth(),
                                                        label = {
                                                            Text("Sub County")
                                                        }
                                                    )

                                                    OutlinedTextField(
                                                        value = profile.county,
                                                        onValueChange = onCountyChanged,
                                                        modifier = Modifier.fillMaxWidth(),
                                                        label = {
                                                            Text("County")
                                                        }
                                                    )

                                                    OutlinedTextField(
                                                        value = profile.country,
                                                        onValueChange = onCountryChanged,
                                                        modifier = Modifier.fillMaxWidth(),
                                                        label = {
                                                            Text("Country")
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }
        // =====================================================
// =====================================================
// SAVE BUTTON
// =====================================================

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Profile")
        }

// =====================================================
// VALIDATION MESSAGE
// =====================================================

        validationMessage?.let {

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {

                Text(
                    text = it,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

// =====================================================
// SUCCESS MESSAGE
// =====================================================

        if (profileSaved) {

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE8F5E9)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "✓ Profile Saved Successfully",
                        color = Color(0xFF2E7D32),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Your profile information has been saved.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

                                }
                            }