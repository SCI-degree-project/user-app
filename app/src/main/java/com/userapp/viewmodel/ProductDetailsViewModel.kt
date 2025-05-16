package com.userapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.userapp.data.ProductRepository
import com.userapp.model.ProductDetails
import com.userapp.viewmodel.uistate.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ProductDetailsViewModel @AssistedInject constructor(
    private val repository: ProductRepository,
    @Assisted("tenantId") private val tenantId: String,
    @Assisted("productId") private val productId: String
) : ViewModel() {

    private val _productState = MutableStateFlow<UiState<ProductDetails>>(UiState.Loading)
    val productState: StateFlow<UiState<ProductDetails>> = _productState

    init {
        fetchProductDetails()
    }

    fun fetchProductDetails() {
        viewModelScope.launch {
            _productState.value = UiState.Loading
            try {
                val result = repository.getProductById(tenantId, productId)
                _productState.value = UiState.Success(result)
            } catch (e: Exception) {
                _productState.value = UiState.Error("Error loading product: ${e.message ?: "Unknown error"}")
                e.printStackTrace()
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("tenantId") tenantId: String,
            @Assisted("productId") productId: String
        ): ProductDetailsViewModel
    }
}
