package com.example.culina.dailyCooking.database

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import com.example.culina.dailyCooking.database.dto.NewDailyCookingDto
import java.util.Date


interface DailyCookingRepository {
    suspend fun createDailyCooking(
        dailyCooking: DailyCooking, imageName: String,
        imageUri: Uri, contentResolver: ContentResolver
    ): Boolean

    suspend fun getAllDailyCooking(): List<NewDailyCookingDto>?
    suspend fun getDailyCooking(id: String): NewDailyCookingDto?
    suspend fun getDailyCookingByDate(date: Date): NewDailyCookingDto?
    suspend fun deleteDailyCooking(id: String)
    suspend fun updateDailyCooking(
        id: String,
        name: String,
        ingredients: Set<String>,
        imageName: String,
        imageFile: ByteArray,
        rating: Int
    )

    suspend fun getDailyCookingImage(path: String): Bitmap?
}