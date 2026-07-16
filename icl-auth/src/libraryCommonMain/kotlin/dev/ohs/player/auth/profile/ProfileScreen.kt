package dev.ohs.player.auth.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onBack: () -> Unit
) {
    val uiState = viewModel.uiState
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.profileSaved) {
        if (viewModel.profileSaved) {
            snackbarHostState.showSnackbar(
                message = "Profile saved successfully!",
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Personal Information Section
            ExpandableSection(
                title = "Personal Information",
                initiallyExpanded = true
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = uiState.firstName,
                        onValueChange = { viewModel.updateFirstName(it) },
                        label = { Text("First Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.lastName,
                        onValueChange = { viewModel.updateLastName(it) },
                        label = { Text("Last Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    GenderDropdown(
                        selectedGender = uiState.gender,
                        onGenderSelected = { viewModel.updateGender(it) }
                    )

                    OutlinedTextField(
                        value = uiState.dateOfBirth,
                        onValueChange = { viewModel.updateDateOfBirth(it) },
                        label = { Text("Date of Birth (YYYY-MM-DD)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Contact Information Section
            ExpandableSection(title = "Contact Information") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = { viewModel.updateEmail(it) },
                        label = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.phone,
                        onValueChange = { viewModel.updatePhone(it) },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Employment Information Section (Optional)
            ExpandableSection(title = "Employment Information (Optional)") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = uiState.idNumber,
                        onValueChange = { viewModel.updateIdNumber(it) },
                        label = { Text("ID Number (Optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.role,
                        onValueChange = { viewModel.updateRole(it) },
                        label = { Text("Role (Optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.practitionerRole,
                        onValueChange = { viewModel.updatePractitionerRole(it) },
                        label = { Text("Practitioner Role (Optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Location Information Section (Optional)
            ExpandableSection(title = "Location Information (Optional)") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = uiState.facility,
                        onValueChange = { viewModel.updateFacility(it) },
                        label = { Text("Facility (Optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.ward,
                        onValueChange = { viewModel.updateWard(it) },
                        label = { Text("Ward (Optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.subCounty,
                        onValueChange = { viewModel.updateSubCounty(it) },
                        label = { Text("Sub-County (Optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.county,
                        onValueChange = { viewModel.updateCounty(it) },
                        label = { Text("County (Optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.country,
                        onValueChange = { viewModel.updateCountry(it) },
                        label = { Text("Country (Optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.saveProfile() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Profile")
            }

            viewModel.validationMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ExpandableSection(
    title: String,
    initiallyExpanded: Boolean = false,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand"
                )
            }
            AnimatedVisibility(visible = expanded) {
                Box(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                    content()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropdown(
    selectedGender: String,
    onGenderSelected: (String) -> Unit
) {
    val options = listOf("Male", "Female", "Other")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedGender,
            onValueChange = {},
            readOnly = true,
            label = { Text("Gender") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onGenderSelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}
