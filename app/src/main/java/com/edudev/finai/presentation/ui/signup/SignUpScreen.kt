package com.edudev.finai.presentation.ui.signup

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.edudev.finai.R
import com.edudev.finai.presentation.components.loginScreen.ProfileImagePicker
import com.edudev.finai.presentation.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(onSignUpSuccess: () -> Unit, onNavigateToLogin: () -> Unit, viewModel: SignUpViewModel = hiltViewModel()) {
    val signUpUiState by viewModel.signUpState.collectAsState()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imageCropper =
        rememberLauncherForActivityResult(CropImageContract()) {
            if (it.isSuccessful) {
                selectedImageUri = it.uriContent
                viewModel.onPhotoUriChanged(it.uriContent)
            }
        }

    val singlePhotoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                uri?.let {
                    val cropOptions =
                        CropImageOptions().apply {
                            guidelines = CropImageView.Guidelines.ON
                            cropShape = CropImageView.CropShape.RECTANGLE
                            fixAspectRatio = true
                            aspectRatioX = 1
                            aspectRatioY = 1
                        }
                    val contractOptions = CropImageContractOptions(uri, cropOptions)
                    imageCropper.launch(contractOptions)
                }
            }
        )

    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.create_account_title), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        ProfileImagePicker(
            imageUri = selectedImageUri,
            onClick = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = signUpUiState.fullName,
            onValueChange = { viewModel.onFullNameChanged(it) },
            label = { Text(stringResource(R.string.label_full_name)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = signUpUiState.email,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = { Text(stringResource(R.string.label_email)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = signUpUiState.password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            label = { Text(stringResource(R.string.label_password)) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = signUpUiState.confirmPassword,
            onValueChange = { viewModel.onConfirmPasswordChanged(it) },
            label = { Text(stringResource(R.string.label_confirm_password)) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = signUpUiState.signUpError != null
        )

        signUpUiState.signUpError?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (signUpUiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    viewModel.signUp(
                        signUpUiState.fullName,
                        signUpUiState.email,
                        signUpUiState.password,
                        signUpUiState.confirmPassword,
                        imageUri = selectedImageUri
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.btn_create_account))
            }
        }

        TextButton(onClick = onNavigateToLogin) {
            Text(stringResource(R.string.link_already_have_account))
        }
    }

    LaunchedEffect(signUpUiState) {
        if (signUpUiState.signUpSuccess) {
            onSignUpSuccess()
        }
    }
}
