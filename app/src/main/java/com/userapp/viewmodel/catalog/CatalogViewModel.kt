package com.userapp.viewmodel.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.userapp.data.ProductRepository
import com.userapp.model.ProductItem
import com.userapp.viewmodel.uistate.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {
    private val _productsState = MutableStateFlow<UiState<List<ProductItem>>>(UiState.Loading)
    val productsState: StateFlow<UiState<List<ProductItem>>> = _productsState

    private var page = 0
    private val size = 20
    private val tenantId = "3fa85f64-5717-4562-b3fc-2c963f66afa6"

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            _productsState.value = UiState.Loading
            try {
                val products = repository.getProducts(tenantId, page, size)
                _productsState.value = UiState.Success(products.content)
            } catch (e: Exception) {
                _productsState.value = UiState.Error("Error loading products: ${e.message ?: "Unknown error"}")
                e.printStackTrace()
            }
        }
    }
}


