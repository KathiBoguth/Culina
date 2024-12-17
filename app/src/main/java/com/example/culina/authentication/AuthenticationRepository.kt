package com.example.culina.authentication

import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.StateFlow

interface AuthenticationRepository {

    suspend fun signIn(email: String, password: String): SignInUpResult
    suspend fun signUp(displayName: String, email: String, password: String): SignInUpResult
    suspend fun getCurrentSession(): UserSession?
    suspend fun getSessionStatus(): StateFlow<SessionStatus>
    suspend fun signOut(): Boolean
}
