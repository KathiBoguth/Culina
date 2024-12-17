package com.example.culina.authentication.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.culina.authentication.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _signedInStatus = MutableStateFlow(SignedInStatus.Loading)
    val signedInStatus = _signedInStatus.asStateFlow()

    init {
        viewModelScope.launch {
            authenticationRepository.getSessionStatus().collect {
                _signedInStatus.value = when (it) {
                    is SessionStatus.Authenticated -> SignedInStatus.SignedIn
                    SessionStatus.Initializing -> SignedInStatus.Loading
                    is SessionStatus.NotAuthenticated -> SignedInStatus.NoSession
                    is SessionStatus.RefreshFailure -> SignedInStatus.NoSession
                }
            }

        }
    }

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onSignIn() {
        viewModelScope.launch {
            authenticationRepository.signIn(
                email = _email.value,
                password = _password.value
            )
        }
    }
}

enum class SignedInStatus {
    Loading, SignedIn, NoSession
}
