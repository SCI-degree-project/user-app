package com.userapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.userapp.model.Product
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            delay(1000)
            _products.value = listOf(
                Product("1", "Chair", "", 150.0, "https://images.unsplash.com/photo-1670103626902-2e70a441c7a6?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTZ8fGZ1cm5pdHVyZXxlbnwwfHwwfHx8MA%3D%3D", listOf()),
                Product("2", "Table", "", 250.0, "https://images.unsplash.com/photo-1718049720161-7eabebf6d8db?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8ZnVybml0dXJlfGVufDB8fDB8fHww", listOf()),
                Product("3", "Modern Chair", "", 230.0, "https://images.unsplash.com/photo-1718049720161-7eabebf6d8db?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8ZnVybml0dXJlfGVufDB8fDB8fHww", listOf()),
                Product("4", "Old Table", "", 780.0, "https://plus.unsplash.com/premium_photo-1692009403831-b94463579caa?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTN8fGZ1cm5pdHVyZXxlbnwwfHwwfHx8MA%3D%3D", listOf()),
                Product("5", "Old Table", "", 780.0, "https://images.unsplash.com/photo-1702018706865-e5306a8fa007?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTF8fGZ1cm5pdHVyZXxlbnwwfHwwfHx8MA%3D%3D", listOf()),
                Product("6", "Old Table", "", 780.0, "https://images.unsplash.com/photo-1718049720161-7eabebf6d8db?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8ZnVybml0dXJlfGVufDB8fDB8fHww", listOf()),
                Product("7", "Old Table", "", 780.0, "https://images.unsplash.com/photo-1670103626902-2e70a441c7a6?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTZ8fGZ1cm5pdHVyZXxlbnwwfHwwfHx8MA%3D%3D", listOf()),
                Product("8", "Old Tableeeeaee eeeaeeee eeeeee", "", 780.0, "https://images.unsplash.com/photo-1702018706865-e5306a8fa007?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTF8fGZ1cm5pdHVyZXxlbnwwfHwwfHx8MA%3D%3D", listOf()),
                Product("9", "Old Tableeee aeeeeeaeeeee eeeee", "", 780.0, "https://plus.unsplash.com/premium_photo-1692009403831-b94463579caa?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTN8fGZ1cm5pdHVyZXxlbnwwfHwwfHx8MA%3D%3D", listOf()),
            )
        }
    }
}
