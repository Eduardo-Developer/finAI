package com.edudev.finai.presentation.ui.profile_edit

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.edudev.finai.R
import com.edudev.finai.presentation.components.FinAiButton
import com.edudev.finai.presentation.components.FinAiTopAppBar
import com.edudev.finai.presentation.components.addTransactionScreen.FormField
import com.edudev.finai.presentation.viewmodel.ProfileEditUiState
import com.edudev.finai.presentation.viewmodel.ProfileEditViewModel
import com.edudev.finai.ui.theme.Emerald
import com.edudev.finai.ui.theme.FinAITheme
import com.edudev.finai.ui.theme.OnSurfaceWhite
import com.edudev.finai.ui.theme.Onyx
import com.edudev.finai.ui.theme.SurfaceContainerHighest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(onNavigateBack: () -> Unit, viewModel: ProfileEditViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    val photoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            viewModel.onImageSelected(uri)
        }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateBack()
        }
    }

    ProfileEditContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPhoneChange = viewModel::onPhoneChange,
        onSaveProfile = viewModel::saveProfile,
        onPickImage = { photoPickerLauncher.launch("image/*") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditContent(
    uiState: ProfileEditUiState,
    onNavigateBack: () -> Unit,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onSaveProfile: () -> Unit,
    onPickImage: () -> Unit
) {
    val profileBitmap =
        remember(uiState.imageUrl, uiState.newImageBase64) {
            val base64 = uiState.newImageBase64 ?: uiState.imageUrl
            base64?.let {
                try {
                    val decodedBytes = Base64.decode(it, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                } catch (e: Exception) {
                    null
                }
            }
        }

    Scaffold(
        containerColor = Onyx,
        topBar = {
            FinAiTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.edit_profile_title),
                        style =
                        MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceWhite
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Emerald
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Avatar Section
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(
                    modifier =
                    Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(SurfaceContainerHighest)
                        .border(2.dp, Emerald.copy(alpha = 0.1f), CircleShape)
                        .shadow(elevation = 32.dp, shape = CircleShape, spotColor = Emerald.copy(alpha = 0.1f))
                        .clickable { onPickImage() },
                    contentAlignment = Alignment.Center
                ) {
                    if (profileBitmap != null) {
                        Image(
                            bitmap = profileBitmap.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else if (uiState.selectedImageUri != null) {
                        AsyncImage(
                            model = uiState.selectedImageUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Emerald.copy(alpha = 0.5f)
                        )
                    }
                }

                Box(
                    modifier =
                    Modifier
                        .size(36.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh, CircleShape)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f), CircleShape)
                        .clickable { onPickImage() }
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = Emerald,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                FormField(
                    label = stringResource(R.string.label_full_name_caps),
                    value = uiState.fullName,
                    onValueChange = onNameChange,
                    icon = Icons.Default.Person,
                    placeholder = "Sarah Jenkins"
                )

                FormField(
                    label = stringResource(R.string.label_email_address),
                    value = uiState.email,
                    onValueChange = onEmailChange,
                    icon = Icons.Default.Email,
                    placeholder = "sarah.jenkins@example.com"
                )

                FormField(
                    label = stringResource(R.string.label_phone_number_caps),
                    value = uiState.phoneNumber,
                    onValueChange = onPhoneChange,
                    icon = Icons.Default.Call,
                    placeholder = "+1 (555) 123-4567"
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            FinAiButton(
                onClick = onSaveProfile,
                isLoading = uiState.isLoading,
                text = stringResource(R.string.btn_save_changes),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileEditContentPreview() {
    FinAITheme {
        ProfileEditContent(
            uiState =
            ProfileEditUiState(
                fullName = "Sarah Jenkins",
                email = "sarah.jenkins@example.com",
                phoneNumber = "+1 (555) 123-4567"
            ),
            onNavigateBack = {},
            onNameChange = {},
            onEmailChange = {},
            onPhoneChange = {},
            onSaveProfile = {},
            onPickImage = {}
        )
    }
}
