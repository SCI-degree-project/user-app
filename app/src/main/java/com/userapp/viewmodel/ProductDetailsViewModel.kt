package com.userapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.userapp.data.ProductRepository
import com.userapp.model.ProductDetails
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

    private val _product = MutableStateFlow<ProductDetails?>(null)
    val product: StateFlow<ProductDetails?> = _product

    init {
        fetchProductDetails()
    }

    private fun fetchProductDetails() {
        viewModelScope.launch {
            try {
                val result = repository.getProductById(tenantId, productId)

                Log.d("ProductDetail", "Received: $result")
                val withGallery = result.copy(
                    gallery = listOf("https://via.placeholder.com/600x400?text=Image+1")
                )

                _product.value = withGallery
            } catch (e: Exception) {
                _product.value = null
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
