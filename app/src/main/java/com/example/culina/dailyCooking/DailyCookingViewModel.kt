package com.example.culina.dailyCooking

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.culina.dailyCooking.database.DailyCooking
import com.example.culina.dailyCooking.database.DailyCookingRepository
import com.example.culina.dailyCooking.database.dto.NewDailyCookingDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class DailyCookingViewModel @Inject constructor(
    private val dailyCookingRepository: DailyCookingRepository,
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: Flow<String> = _name

    private val _ingredients = MutableStateFlow(emptySet<String>())
    val ingredients: Flow<Set<String>> = _ingredients

    private val _rating = MutableStateFlow(0)
    val rating: Flow<Int> = _rating

    private val _imageBitmap: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    val imageBitmap: Flow<Bitmap?> = _imageBitmap

    init {
        getDailyCookingForToday()
    }

    fun getDailyCookingForToday() {
        viewModelScope.launch {
            val result =
                dailyCookingRepository.getDailyCookingByDate(Date())
                    ?.asDomainModel()
            if (result != null) {
                _name.emit(result.name)
                _ingredients.emit(result.ingredients)
                _rating.emit(result.rating)
                if (result.image != null) {
                    val imageByteArray = dailyCookingRepository.getDailyCookingImage(result.image)
                    if (imageByteArray != null) {
                        _imageBitmap.emit(
                            BitmapFactory.decodeByteArray(
                                imageByteArray,
                                0,
                                imageByteArray.size
                            )
                        )
                    }
                }
            }
        }
    }

    fun onNameChange(name: String) {
        _name.value = name
    }

    fun onRatingChange(rating: Int) {
        _rating.value = rating
    }

    fun onIngredientsChange(ingredients: Set<String>) {
        _ingredients.value = ingredients
    }

    fun onSaveDailyCooking(
        image: ByteArray,
        imageName: String,
    ) {
        viewModelScope.launch {
            dailyCookingRepository.createDailyCooking(
                DailyCooking(
                    name = _name.value,
                    ingredients = _ingredients.value,
                    image = imageName,
                    rating = _rating.value,
                ),
                imageName = imageName,
                imageFile = image
            )
        }
    }


    private fun NewDailyCookingDto.asDomainModel(): DailyCooking {
        return DailyCooking(
            name = this.name,
            ingredients = this.ingredients,
            image = this.image,
            rating = this.rating
        )
    }
}
