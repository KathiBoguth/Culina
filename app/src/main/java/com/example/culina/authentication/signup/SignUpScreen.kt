package com.example.culina.authentication.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.culina.authentication.SignInUpResult
import com.example.culina.authentication.signin.PasswordVisibilityToggleIcon
import com.example.culina.common.BackgroundPanel
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState,
    innerPadding: PaddingValues,
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()

    val email = viewModel.email.collectAsState(initial = "")
    val password = viewModel.password.collectAsState()
    val displayName = viewModel.displayName.collectAsState()

    var showPassword by remember { mutableStateOf(false) }

    BackgroundPanel(innerPadding) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            OutlinedTextField(
                label = {
                    Text(
                        text = "Display name",
                    )
                },
                maxLines = 1,
                shape = RoundedCornerShape(32),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                value = displayName.value,
                onValueChange = {
                    viewModel.onDisplayNameChange(it)
                },
            )
            OutlinedTextField(
                label = {
                    Text(
                        text = "Email",
                    )
                },
                maxLines = 1,
                shape = RoundedCornerShape(32),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                value = email.value,
                onValueChange = {
                    viewModel.onEmailChange(it)
                },
            )
            OutlinedTextField(
                label = {
                    Text(
                        text = "Password",
                    )
                },
                maxLines = 1,
                shape = RoundedCornerShape(32),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    PasswordVisibilityToggleIcon(
                        showPassword = showPassword,
                        onTogglePasswordVisibility = { showPassword = !showPassword })
                },
                value = password.value,
                onValueChange = {
                    viewModel.onPasswordChange(it)
                },
            )
            val localSoftwareKeyboardController = LocalSoftwareKeyboardController.current
            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
                onClick = {
                    localSoftwareKeyboardController?.hide()
                    val resultFlow = viewModel.onSignUp()
                    coroutineScope.launch {
                        resultFlow.take(2).collect {
                            val message = when (it) {
                                is SignInUpResult.Failed -> "Create account failed. ${it.errorMessage}"
                                SignInUpResult.Loading -> "Please wait.."
                                SignInUpResult.Success -> "Create account successfully. Sign in now!"
                            }
                            snackBarHostState.showSnackbar(
                                message = message,
                                duration = SnackbarDuration.Short
                            )
                            if (it is SignInUpResult.Success) {
                                navController.navigate("signIn")
                            }
                        }
                    }
                }) {
                Text("Sign up")
            }
        }
    }

}
