package com.userapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.userapp.data.ProductRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import dagger.assisted.AssistedFactory


class Product3DModelViewModel @AssistedInject constructor(
    private val repository: ProductRepository,
    @Assisted("productId") private val productId: String
) : ViewModel() {

    private val _modelUrl = MutableStateFlow<String?>(null)
    val modelUrl: StateFlow<String?> = _modelUrl

    init {
        viewModelScope.launch {
            try {
                val product = repository.getProductById(productId)
                _modelUrl.value = product.model.takeIf { it.isNotEmpty() }
            } catch (e: Exception) {
                _modelUrl.value = null
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("productId") productId: String
        ): Product3DModelViewModel
    }
}

