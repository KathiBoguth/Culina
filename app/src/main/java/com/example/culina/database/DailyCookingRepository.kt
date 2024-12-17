package com.example.culina.database

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import com.example.culina.database.dto.DisplayDailyCookingDto
import com.example.culina.database.dto.NewDailyCookingDto
import java.util.Date


interface DailyCookingRepository {
    suspend fun createDailyCooking(
        dailyCooking: DailyCooking, imageName: String,
        imageUri: Uri, contentResolver: ContentResolver
    ): Boolean

    suspend fun getAllDailyCooking(): List<DisplayDailyCookingDto>?
    suspend fun getDailyCooking(id: String): NewDailyCookingDto?
    suspend fun getDailyCookingByDate(date: Date, userId: String?): NewDailyCookingDto?
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