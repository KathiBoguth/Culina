package com.example.culina.dailyCooking.database

import com.example.culina.dailyCooking.database.dto.NewDailyCookingDto
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

class DailyCookingRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val storage: Storage,
) : DailyCookingRepository {
    override suspend fun createDailyCooking(dailyCooking: DailyCooking): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val newDailyCookingDto = NewDailyCookingDto(
                    name = dailyCooking.name,
                    ingredients = dailyCooking.ingredients,
                    image = dailyCooking.image,
                    rating = dailyCooking.rating,
                )
                postgrest.from("dailycooking").insert(newDailyCookingDto)
                true
            }
            true
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getAllDailyCooking(): List<NewDailyCookingDto>? {
        return withContext(Dispatchers.IO) {
            postgrest.from("dailycooking")
                .select().decodeList<NewDailyCookingDto>()
        }
    }


    override suspend fun getDailyCooking(id: String): NewDailyCookingDto? {
        return try {
            withContext(Dispatchers.IO) {
                postgrest.from("dailycooking").select {
                    filter {
                        eq("id", id)
                    }
                }.decodeSingle<NewDailyCookingDto>()
            }
        } catch (e: kotlin.Exception) {
            println(e.message)
            null
        }
    }

    override suspend fun getDailyCookingByDate(date: Date): NewDailyCookingDto? {
        val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        return try {
            withContext(Dispatchers.IO) {
                postgrest.from("dailycooking").select {
                    filter {
                        and {
                            gte(
                                "created_at",
                                localDate.atStartOfDay()
                            )
                            lte(
                                "created_at",
                                localDate.plusDays(1).atStartOfDay()
                            )
                        }
                    }
                }.decodeSingle<NewDailyCookingDto>()
            }
        } catch (e: kotlin.Exception) {
            println(e.message)
            null
        }
    }

    override suspend fun deleteDailyCooking(id: String) {
        return withContext(Dispatchers.IO) {
            postgrest.from("dailycooking").delete {
                filter {
                    eq("id", id)
                }
            }
        }
    }

    override suspend fun updateDailyCooking(
        id: String,
        name: String,
        ingredients: Set<String>,
        imageName: String,
        imageFile: ByteArray,
        rating: Int
    ) {
        withContext(Dispatchers.IO) {
            println(imageFile.isNotEmpty())
            if (imageFile.isNotEmpty()) {
                val image =
                    storage.from("dailyMealImage").upload(
                        path = "$imageName.png",
                        data = imageFile,
                        options = { upsert = true }
                    )
                println(image)
                val result = postgrest.from("dailycooking").update({
                    set("name", name)
                    set("ingredients", ingredients)
                    set("image", image.path)
                    set("rating", rating)
                }) {
                    filter {
                        eq("id", id)
                    }
                }
                println(result.data)
            } else {
                postgrest.from("dailycooking").update({
                    set("name", name)
                    set("ingredients", ingredients)
                    set("rating", rating)
                }) {
                    filter {
                        eq("id", id)
                    }
                }
            }
        }
    }
}
