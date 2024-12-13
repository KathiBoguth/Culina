package com.example.culina.authentication.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.culina.authentication.AuthenticationRepository
import com.example.culina.authentication.SignInUpResult
import com.example.culina.database.ScoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val scoreRepository: ScoreRepository,
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: Flow<String> = _email

    private val _password = MutableStateFlow("")
    val password = _password

    private val _displayName = MutableStateFlow("")
    val displayName = _displayName

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
            scoreRepository.addScoreEntry()
        }
        return flow
    }
}
