package com.example.culina.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.culina.authentication.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    fun signOut(): MutableStateFlow<Boolean> {
        val flow = MutableStateFlow(false)
        viewModelScope.launch {
            val result = authenticationRepository.signOut()
            flow.emit(result)
        }
        return flow
    }
}