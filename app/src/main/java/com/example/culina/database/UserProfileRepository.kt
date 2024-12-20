package com.example.culina.database

interface UserProfileRepository {

    suspend fun addEntry(userId: String, displayName: String): Boolean
    suspend fun getScoreByUser(id: String?): Int?
    suspend fun updateScore(newScore: Int, userId: String): Boolean

}