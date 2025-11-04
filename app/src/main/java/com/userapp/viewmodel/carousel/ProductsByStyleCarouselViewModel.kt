package com.userapp.viewmodel.carousel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.userapp.data.ProductRepository
import com.userapp.model.ProductItem
import com.userapp.model.Style
import com.userapp.viewmodel.uistate.UiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductsByStyleCarouselViewModel @AssistedInject constructor(
    private val repository: ProductRepository,
    @Assisted("tenantId") private val tenantId: String,
    @Assisted("style") private val style: Style,
    @Assisted("page") private val page: Int,
    @Assisted("size") private val size: Int,
) : ViewModel() {
    private val _productsState = MutableStateFlow<UiState<List<ProductItem>>>(UiState.Loading)
    val productsState: StateFlow<UiState<List<ProductItem>>> = _productsState

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            _productsState.value = UiState.Loading
            try {
                val pagedProducts = repository.getProductsByStyle(tenantId, style, page, size)

                _productsState.value = UiState.Success(pagedProducts.content)
            } catch (e: Exception) {
                _productsState.value = UiState.Error("Error loading products: ${e.message ?: "Unknown error"}")
                e.printStackTrace()
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("tenantId") tenantId: String,
            @Assisted("style") style: Style,
            @Assisted("page") page: Int,
            @Assisted("size") size: Int,
        ): ProductsByStyleCarouselViewModel
    }
}
