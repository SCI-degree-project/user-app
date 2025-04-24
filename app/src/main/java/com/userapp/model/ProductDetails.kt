package com.userapp.model

data class ProductDetails(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val gallery: List<String>,
    val style: String,
    val materials: String
)
