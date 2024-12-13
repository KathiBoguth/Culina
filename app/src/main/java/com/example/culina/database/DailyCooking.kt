package com.example.culina.database

data class DailyCooking(

    val name: String,
    val ingredients: Set<String>,
    val image: String?,
    val rating: Int,
//    val id: UUID,
)