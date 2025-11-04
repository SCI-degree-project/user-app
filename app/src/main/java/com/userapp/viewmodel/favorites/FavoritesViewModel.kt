package com.userapp.viewmodel.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.userapp.data.ProductRepository
import com.userapp.database.dao.ProductDao
import com.userapp.database.entities.FavoriteProductEntity
import com.userapp.model.ProductItem
import com.userapp.viewmodel.uistate.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val dao: ProductDao
) : ViewModel() {

    private val _favoritesState = MutableStateFlow<UiState<List<ProductItem>>>(UiState.Loading)
    val favoritesState: StateFlow<UiState<List<ProductItem>>> = _favoritesState

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    fun checkIfFavorite(productId: String) {
        viewModelScope.launch {
            _isFavorite.value = dao.getFavoriteProducts().any { it.productId == productId }
        }
    }

    fun getFavorites() {
        viewModelScope.launch {
            _favoritesState.value = UiState.Loading
            try {
                val favoriteEntities = dao.getFavoriteProducts()
                val ids = favoriteEntities.map { it.productId }
                if (ids.isNotEmpty()) {
                    val products = repository.getProductsBatch(ids)
                    _favoritesState.value = UiState.Success(products)
                } else {
                    _favoritesState.value = UiState.Success(emptyList())
                }
            } catch (e: Exception) {
                _favoritesState.value = UiState.Error("Error loading favorites: ${e.message ?: "Unknown error"}")
                e.printStackTrace()
            }
        }
    }

    fun toggleFavorite(productId: String) {
        viewModelScope.launch {
            if (dao.getFavoriteProducts().any { it.productId == productId }) {
                dao.removeFavoriteProduct(productId)
            } else {
                dao.insertFavoriteProduct(FavoriteProductEntity(productId = productId))
            }
            checkIfFavorite(productId)
        }
    }
}
