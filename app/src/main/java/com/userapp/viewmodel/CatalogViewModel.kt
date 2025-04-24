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
                ProductItem("1", "Chair", "https://images.unsplash.com/photo-1670103626902-2e70a441c7a6?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTZ8fGZ1cm5pdHVyZXxlbnwwfHwwfHx8MA%3D%3D"),
                ProductItem("2", "Table", "https://images.unsplash.com/photo-1718049720161-7eabebf6d8db?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8ZnVybml0dXJlfGVufDB8fDB8fHww"),
                ProductItem("3", "Modern Chair", "https://images.unsplash.com/photo-1718049720161-7eabebf6d8db?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8ZnVybml0dXJlfGVufDB8fDB8fHww"),
                ProductItem("4", "Old Table", "https://plus.unsplash.com/premium_photo-1692009403831-b94463579caa?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTN8fGZ1cm5pdHVyZXxlbnwwfHwwfHx8MA%3D%3D"),
                ProductItem("5", "Old Table", "https://images.unsplash.com/photo-1702018706865-e5306a8fa007?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTF8fGZ1cm5pdHVyZXxlbnwwfHwwfHx8MA%3D%3D"),
                ProductItem("6", "Old Table", "https://images.unsplash.com/photo-1718049720161-7eabebf6d8db?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8ZnVybml0dXJlfGVufDB8fDB8fHww"),
                ProductItem("7", "Old Table", "https://images.unsplash.com/photo-1670103626902-2e70a441c7a6?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTZ8fGZ1cm5pdHVyZXxlbnwwfHwwfHx8MA%3D%3D"),
                ProductItem("8", "Old Tableeeeaee eeeaeeee eeeeee", "https://images.unsplash.com/photo-1702018706865-e5306a8fa007?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTF8fGZ1cm5pdHVyZXxlbnwwfHwwfHx8MA%3D%3D"),
                ProductItem("9", "Old Tableeee aeeeeeaeeeee eeeee", "https://plus.unsplash.com/premium_photo-1692009403831-b94463579caa?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTN8fGZ1cm5pdHVyZXxlbnwwfHwwfHx8MA%3D%3D"),
            )
        }
    }
}
