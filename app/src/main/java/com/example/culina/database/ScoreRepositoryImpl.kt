package com.example.culina.database

import com.example.culina.database.dto.ScoreDto
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class ScoreRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
) : ScoreRepository {
    override suspend fun addScoreEntry(): Boolean {
        return try {

            withContext(Dispatchers.IO) {
                val scoreDto = ScoreDto(
                    score = 0,
                )
                postgrest.from("scores").insert(scoreDto)
                true
            }
            true
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getScoreByUser(id: String?): Int? {
        if (id == null) {
            return null
        }
        return try {
            withContext(Dispatchers.IO) {
                val result = postgrest.from("scores").select {
                    filter {
                        eq("user_id", id)
                    }
                }.decodeSingle<ScoreDto>()
                return@withContext result.score
            }
        } catch (e: kotlin.Exception) {
            println(e.message)
            null
        }
    }

    override suspend fun updateScore(newScore: Int, userId: String): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                postgrest.from("scores").update({
                    set("score", newScore)
                }) {
                    filter {
                        eq("user_id", userId)
                    }
                }
            }
            true
        } catch (e: kotlin.Exception) {
            println(e.message)
            false
        }
    }
}