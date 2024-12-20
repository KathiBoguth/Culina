package com.example.culina.database.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(

    @SerialName("id")
    val userId: String,

    @SerialName("display_name")
    val displayName: String,

    @SerialName("score")
    val score: Int,

    @SerialName("profile_image")
    val profileImage: String? = null,
)