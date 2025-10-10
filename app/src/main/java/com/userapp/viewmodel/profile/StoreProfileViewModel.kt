package com.userapp.viewmodel.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class StoreProfileViewModel : ViewModel() {
    private val _store = mutableStateOf(
        StoreDto(
            name = "Muebles Cochabamba",
            description = "Tienda especializada en muebles de alta calidad para el hogar y oficina.",
            address = "Av. América #123, Cochabamba, Bolivia",
            phone = "+591 70012345",
            facebookURL = "https://www.facebook.com/mueblescocha",
            instagramURL = "https://www.instagram.com/mueblescocha",
            tiktokURL = null
        )
    )
    val store: State<StoreDto> = _store
}

data class StoreDto(
    val name: String,
    val description: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val facebookURL: String? = null,
    val instagramURL: String? = null,
    val tiktokURL: String? = null
)
