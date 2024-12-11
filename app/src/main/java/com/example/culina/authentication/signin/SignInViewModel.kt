package com.example.culina.authentication.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.culina.authentication.AuthenticationRepository
import com.example.culina.authentication.SignInUpResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: Flow<String> = _email

    private val _password = MutableStateFlow("")
    val password = _password

    private val _signedIn = MutableStateFlow(false)
    val signedIn: Flow<Boolean> = _signedIn

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onSignIn(): MutableStateFlow<SignInUpResult> {
        val flow = MutableStateFlow<SignInUpResult>(SignInUpResult.Loading)
        viewModelScope.launch {
            val successful = authenticationRepository.signIn(
                email = _email.value,
                password = _password.value
            )
            _signedIn.value = successful == SignInUpResult.Success
            flow.emit(successful)
        }
        return flow
    }

    fun getCurrentSession(): MutableStateFlow<SignInUpResult> {
        val flow = MutableStateFlow<SignInUpResult>(SignInUpResult.Loading)

        viewModelScope.launch {
            val result = authenticationRepository.getCurrentSession()
            if (result == null) {
                flow.emit(SignInUpResult.Failed("no active session"))
            } else {
                flow.emit(SignInUpResult.Success)

            }
        }
        return flow
    }

}
