package com.example.culina.database.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DisplayDailyCookingDto(

    @SerialName("name")
    val name: String,

    @SerialName("ingredients")
    val ingredients: Set<String>,

    @SerialName("image")
    val image: String?,

    @SerialName("rating")
    val rating: Int,

    @SerialName("user_id")
    val userId: String,

    @SerialName("display_name")
    var userName: String? = null
)