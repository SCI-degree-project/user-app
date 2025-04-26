package com.userapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.userapp.model.ProductItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatalogViewModel : ViewModel() {

    private val _products = MutableStateFlow<List<ProductItem>>(emptyList())
    val products: StateFlow<List<ProductItem>> = _products

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            delay(1000)
            _products.value = listOf(
                ProductItem("1", "White Sofa", "https://images.unsplash.com/photo-1670103626902-2e70a441c7a6?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTZ8fGZ1cm5pdHVyZXxlbnwwfHwwfHx8MA%3D%3D"),
                ProductItem("2", "Modern Chair", "https://images.unsplash.com/photo-1718049720161-7eabebf6d8db?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8ZnVybml0dXJlfGVufDB8fDB8fHww"),
                ProductItem("3", "Green Sofa", "https://images.unsplash.com/photo-1555041469-a586c61ea9bc?q=80&w=3870&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
                ProductItem("4", "Modern Blue Sofa", "https://plus.unsplash.com/premium_photo-1692009403831-b94463579caa?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTN8fGZ1cm5pdHVyZXxlbnwwfHwwfHx8MA%3D%3D"),
                ProductItem("5", "Old Table", "https://images.unsplash.com/photo-1702018706865-e5306a8fa007?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTF8fGZ1cm5pdHVyZXxlbnwwfHwwfHx8MA%3D%3D"),
                ProductItem("6", "High Stool", "https://images.unsplash.com/photo-1503602642458-232111445657?q=80&w=3987&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
                ProductItem("7", "Small White Table", "https://images.unsplash.com/photo-1499933374294-4584851497cc?q=80&w=2448&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
                ProductItem("8", "Brown Minimalist Stool", "https://images.unsplash.com/photo-1540809799-5da9372c3f64?q=80&w=2921&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
                ProductItem("9", "White Chair", "https://images.unsplash.com/photo-1526827826797-7b05204a22ef?q=80&w=3648&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
                ProductItem("10", "Simple White Chair", "https://images.unsplash.com/photo-1506326426992-32b61983f2fd?q=80&w=2743&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
                ProductItem("11", "Minimalist Desk", "https://images.unsplash.com/photo-1486946255434-2466348c2166?q=80&w=3393&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
                ProductItem("12", "Brown Closet", "https://images.unsplash.com/photo-1558997519-83ea9252edf8?q=80&w=3987&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
            )
        }
    }
}
