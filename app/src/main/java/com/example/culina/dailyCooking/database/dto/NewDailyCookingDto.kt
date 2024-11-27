package com.example.culina.dailyCooking.database.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewDailyCookingDto (

    @SerialName("name")
    val name: String,

    @SerialName("ingredients")
    val ingredients: Set<String>,

    @SerialName("image")
    val image: String?,

    @SerialName("rating")
    val rating: Int,

)
