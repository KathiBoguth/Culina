package com.example.culina.authentication.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.culina.authentication.AuthenticationRepository
import com.example.culina.authentication.SignInUpResult
import com.example.culina.authentication.signin.SignedInStatus
import com.example.culina.database.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val userProfileRepository: UserProfileRepository,
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: Flow<String> = _email

    private val _password = MutableStateFlow("")
    val password = _password

    private val _displayName = MutableStateFlow("")
    val displayName = _displayName

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

    fun onDisplayNameChange(name: String) {
        _displayName.value = name
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onSignUp(): MutableStateFlow<SignInUpResult> {
        val flow = MutableStateFlow<SignInUpResult>(SignInUpResult.Loading)
        viewModelScope.launch {
            val result = authenticationRepository.signUp(
                displayName = _displayName.value,
                email = _email.value,
                password = _password.value
            )
            flow.emit(result)
            val userId = authenticationRepository.getCurrentSession()?.user?.id
            if (userId != null) {
                userProfileRepository.addEntry(
                    userId,
                    _displayName.value
                )
            }

        }
        return flow
    }
}
