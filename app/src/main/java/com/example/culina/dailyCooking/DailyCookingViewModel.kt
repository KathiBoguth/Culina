package com.example.culina.dailyCooking

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

    private val _imageUrl = MutableStateFlow("")
    val imageUrl: Flow<String> = _imageUrl

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
            }

        }
    }

    fun onNameChange(name: String) {
        _name.value = name
    }

    fun onRatingChange(rating: Int) {
        _rating.value = rating
    }

    fun onImageChange(url: String) {
        _imageUrl.value = url
    }

    fun onIngredientsChange(ingredients: Set<String>) {
        _ingredients.value = ingredients
    }

    fun onSaveDailyCooking(
        name: String,
        ingredients: Set<String>,
        image: String?,
        rating: Int,
    ) {
        viewModelScope.launch {
            dailyCookingRepository.createDailyCooking(
                DailyCooking(
                    name = name,
                    ingredients = ingredients,
                    image = image,
                    rating = rating,
                )
            )
//            dailyMealRepository.updateDailyMeal(
//                id = _dailyMeal.value?.id ?: "",
//                ingredients = _ingredients.value,
//                name = _name.value,
//                imageFile = image,
//                imageName = "image_${_dailyMeal.value?.id}",
//                rating = _rating.value
//            )
        }
    }


    private fun NewDailyCookingDto.asDomainModel(): DailyCooking {
        return DailyCooking(
//            id = this.id,
            name = this.name,
            ingredients = this.ingredients,
            image = this.image,
            rating = this.rating
        )
    }
}
