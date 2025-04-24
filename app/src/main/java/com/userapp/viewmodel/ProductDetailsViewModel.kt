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
                description = "Comfortable and stylish chair.",
                price = 230.0,
                gallery = listOf(
                    "https://images.unsplash.com/photo-1718049720161-7eabebf6d8db?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8ZnVybml0dXJlfGVufDB8fDB8fHww",
                    "https://images.unsplash.com/photo-1718049720161-7eabebf6d8db?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8ZnVybml0dXJlfGVufDB8fDB8fHww",
                    "https://images.unsplash.com/photo-1718049720161-7eabebf6d8db?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8ZnVybml0dXJlfGVufDB8fDB8fHww",
                    "https://images.unsplash.com/photo-1718049720161-7eabebf6d8db?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8ZnVybml0dXJlfGVufDB8fDB8fHww",
                    "https://images.unsplash.com/photo-1718049720161-7eabebf6d8db?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8ZnVybml0dXJlfGVufDB8fDB8fHww"
                )
            )
            _product.value = fetchedProduct
        }
    }
}