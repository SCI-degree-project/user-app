package com.userapp.model

data class StoreDetails(
    val id: String,
    val name: String,
    val description: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val facebookURL: String? = null,
    val instagramURL: String? = null,
    val tiktokURL: String? = null
)
