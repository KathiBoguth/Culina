package com.example.culina.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.culina.authentication.AuthenticationRepository
import com.example.culina.database.ScoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val scoreRepository: ScoreRepository
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: Flow<String> = _name

    private val _score = MutableStateFlow(0)
    val score: Flow<Int> = _score

    init {
        getScore()
        getName()
    }

    fun signOut(): MutableStateFlow<Boolean> {
        val flow = MutableStateFlow(false)
        viewModelScope.launch {
            val result = authenticationRepository.signOut()
            flow.emit(result)
        }
        return flow
    }

    fun getScore() {
        viewModelScope.launch {
            val result =
                scoreRepository.getScoreByUser(authenticationRepository.getCurrentSession()?.user?.id)
            if (result != null) {
                _score.value = result
            }
        }
    }

    fun getName() {
        viewModelScope.launch {
            val result =
                authenticationRepository.getCurrentSession()?.user?.userMetadata?.get("display_name")
            if (result != null) {
                _name.value = result.toString().removeSurrounding('"'.toString())
            }
        }
    }
}
