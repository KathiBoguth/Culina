package com.example.culina.authentication

import io.github.jan.supabase.auth.user.UserSession

interface AuthenticationRepository {

    suspend fun signIn(email: String, password: String): SignInUpResult
    suspend fun signUp(displayName: String, email: String, password: String): SignInUpResult
    suspend fun getCurrentSession(): UserSession?
    suspend fun signOut(): Boolean
}
