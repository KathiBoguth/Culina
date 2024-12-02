package com.example.culina.dailyCooking.database

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.culina.dailyCooking.database.dto.NewDailyCookingDto
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

class DailyCookingRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val storage: Storage,
) : DailyCookingRepository {
    override suspend fun createDailyCooking(
        dailyCooking: DailyCooking,
        imageName: String,
        imageUri: Uri,
        contentResolver: ContentResolver
    ): Boolean {
        return try {
            val baos = ByteArrayOutputStream()
            contentResolver.openInputStream(imageUri)
                ?.use {
                    BitmapFactory.decodeStream(it).compress(Bitmap.CompressFormat.JPEG, 50, baos)
                }
            withContext(Dispatchers.IO) {
                val imageByteArray = baos.toByteArray()
                val imageId = if (imageByteArray != null) {
                    storage.from("dailyMealImage").upload(
                        path = imageName,
                        data = imageByteArray,
                        options = { upsert = true }
                    ).path
                } else {
                    null
                }
                val newDailyCookingDto = NewDailyCookingDto(
                    name = dailyCooking.name,
                    ingredients = dailyCooking.ingredients,
                    image = imageId,
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
            if (imageFile.isNotEmpty()) {
                val image =
                    storage.from("dailyMealImage").upload(
                        path = imageName,
                        data = imageFile,
                        options = { upsert = true }
                    )
                postgrest.from("dailycooking").update({
                    set("name", name)
                    set("ingredients", ingredients)
                    set("image", image.path)
                    set("rating", rating)
                }) {
                    filter {
                        eq("id", id)
                    }
                }
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

    override suspend fun getDailyCookingImage(path: String): Bitmap? {
        return try {
            withContext(Dispatchers.IO) {
                val imageByteArray = storage.from("dailyMealImage").downloadPublic(path)
                return@withContext BitmapFactory.decodeByteArray(
                    imageByteArray,
                    0,
                    imageByteArray.size
                )
            }
        } catch (e: kotlin.Exception) {
            println(e.message)
            null
        }
    }
}
