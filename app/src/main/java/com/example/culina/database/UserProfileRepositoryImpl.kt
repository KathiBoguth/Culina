package com.example.culina.database

import com.example.culina.database.dto.UserProfileDto
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
) : UserProfileRepository {
    override suspend fun addEntry(userId: String, displayName: String): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val userProfileDto = UserProfileDto(
                    displayName = displayName,
                    score = 0,
                    profileImage = null,
                    userId = userId
                )
                postgrest.from("user_profiles").insert(userProfileDto)
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
                }.decodeSingle<UserProfileDto>()
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