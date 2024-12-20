package com.example.culina.social

import android.graphics.Bitmap
import androidx.core.graphics.scale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.culina.database.DailyCookingRepository
import com.example.culina.database.dto.DisplayDailyCookingDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocialViewModel @Inject constructor(
    private val dailyCookingRepository: DailyCookingRepository
) : ViewModel() {

    private val _allPosts = MutableStateFlow(listOf<DisplayDailyCookingDto>())
    val allPosts: Flow<List<DisplayDailyCookingDto>> = _allPosts

    private val _allImages = MutableStateFlow<Map<String, Bitmap>>(mapOf())
    val allImages: Flow<Map<String, Bitmap>> = _allImages

    init {
        fetchAllPosts()
    }

    fun fetchAllPosts() {
        viewModelScope.launch {
            val posts = dailyCookingRepository.getAllDailyCooking()
            if (posts == null) {
                return@launch
            }
            _allPosts.value = posts
        }
    }

    fun fetchImage(id: String) {
        viewModelScope.launch {
            val image = getImage(id).await()
            if (image != null) {
                val newMap = _allImages.value.toMutableMap()
                newMap.put(id, image)
                _allImages.emit(newMap)
            }
        }
    }

    fun getImage(imageId: String?): Deferred<Bitmap?> {

        return viewModelScope.async {
            if (imageId == null) {
                return@async null
            }
            val image = dailyCookingRepository.getDailyCookingImage(imageId)
            if (image == null) {
                return@async null
            }
            return@async image.scale(width = 1025, height = 1025)
        }
    }
}