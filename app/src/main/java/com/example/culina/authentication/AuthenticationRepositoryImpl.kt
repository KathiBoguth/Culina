package com.example.culina.authentication

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val auth: Auth
) : AuthenticationRepository {

    override suspend fun signIn(email: String, password: String): SignInUpResult {
        return try {
            auth.signInWith(provider = Email) {
                this.email = email
                this.password = password
            }
            SignInUpResult.Success
        } catch (e: Exception) {
            SignInUpResult.Failed(e.message ?: "Unknown error")
        }
    }

    override suspend fun signUp(
        displayName: String,
        email: String,
        password: String
    ): SignInUpResult {
        return try {
            auth.signUpWith(Email) {
                this.data = buildJsonObject {
                    put("display_name", displayName)
                }
                this.email = email
                this.password = password
            }

            SignInUpResult.Success
        } catch (e: Exception) {
            println("signup exception ${e.message}")
            SignInUpResult.Failed(e.message ?: "Unknown Error")
        }
    }

    override suspend fun getSessionStatus(): StateFlow<SessionStatus> {
        return auth.sessionStatus
    }

    override suspend fun getCurrentSession(): UserSession? {
        return auth.currentSessionOrNull()
    }

    override suspend fun signOut(): Boolean {
        auth.signOut()
        auth.sessionStatus.first { it is SessionStatus.NotAuthenticated }
        return true
    }
}

sealed class SignInUpResult() {
    data object Loading : SignInUpResult()
    data object Success : SignInUpResult()
    data class Failed(val errorMessage: String) : SignInUpResult()
}
