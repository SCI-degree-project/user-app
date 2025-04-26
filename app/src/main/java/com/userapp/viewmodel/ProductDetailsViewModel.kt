package com.userapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.userapp.model.ProductDetails
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductDetailsViewModel(private val productId: String) : ViewModel() {
    private val _product = MutableStateFlow<ProductDetails?>(null)
    val product: StateFlow<ProductDetails?> = _product

    init {
        fetchProductDetails()
    }

    private fun fetchProductDetails() {
        viewModelScope.launch {
            delay(1000)
            val fetchedProduct = ProductDetails(
                id = productId,
                name = "Modern Chair",
                description = "Comfortable and stylish moder chair. Ideal for your modern life.",
                price = 230.0,
                gallery = listOf(
                    "https://images.unsplash.com/photo-1718049720161-7eabebf6d8db?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8ZnVybml0dXJlfGVufDB8fDB8fHww",
                    "https://images.unsplash.com/photo-1718524767499-78ce54e0e188?q=80&w=3651&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    "https://images.unsplash.com/photo-1729603453958-0e7222540051?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1yZWxhdGVkfDN8fHxlbnwwfHx8fHw%3D",
                    "https://images.unsplash.com/photo-1718049720170-273ba3f560aa?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1yZWxhdGVkfDEwfHx8ZW58MHx8fHx8",
                ),
                "Modern",
                "Wood, Leather, Metal, Plastic"
            )
            _product.value = fetchedProduct
        }
    }
}