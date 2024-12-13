package com.example.culina.database

interface ScoreRepository {

    suspend fun addScoreEntry(): Boolean
    suspend fun getScoreByUser(id: String?): Int?
    suspend fun updateScore(newScore: Int, userId: String): Boolean

}