package com.example.culina.authentication.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.culina.R
import com.example.culina.authentication.SignInUpResult
import com.example.culina.common.BackgroundPanel
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState,
    navController: NavController,
    innerPadding: PaddingValues
) {
    val coroutineScope = rememberCoroutineScope()
    val signedIn by viewModel.signedIn.collectAsState(false)

    LaunchedEffect(Unit, signedIn) {
        if (signedIn) {
            navController.navigate("home")
        }
        viewModel.getCurrentSession().collect {
            when (it) {
                is SignInUpResult.Failed -> {}
                SignInUpResult.Loading -> {}
                SignInUpResult.Success -> navController.navigate("home")
            }
        }
    }

    BackgroundPanel(innerPadding) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            val email = viewModel.email.collectAsState(initial = "")
            val password = viewModel.password.collectAsState()
            var showPassword by remember { mutableStateOf(false) }

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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
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
                    val resultFlow = viewModel.onSignIn()

                    coroutineScope.launch {
                        resultFlow.take(2).collect {
                            val message = when (it) {
                                is SignInUpResult.Failed -> "Log in failed. ${it.errorMessage}"
                                SignInUpResult.Loading -> "Please wait.."
                                SignInUpResult.Success -> "Sign in successfully!"
                            }
                            snackBarHostState.showSnackbar(
                                message = message,
                            )
                        }
                    }
                }) {
                Text("Sign in")
            }
            OutlinedButton(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp), onClick = {
                navController.navigate("signup")
            }) {
                Text("Sign up")
            }
        }
    }
}

@Composable
fun PasswordVisibilityToggleIcon(
    showPassword: Boolean,
    onTogglePasswordVisibility: () -> Unit
) {
    val image =
        if (showPassword) Image(
            painter = painterResource(id = R.drawable.baseline_visibility_24),
            contentDescription = "visible"
        ) else Image(
            painter = painterResource(id = R.drawable.baseline_visibility_off_24),
            contentDescription = "hidden"
        )

    IconButton(onClick = onTogglePasswordVisibility) {
        image
    }
}
