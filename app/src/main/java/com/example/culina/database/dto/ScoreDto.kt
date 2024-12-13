package com.example.culina.database.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScoreDto(

    @SerialName("score")
    val score: Int,
)