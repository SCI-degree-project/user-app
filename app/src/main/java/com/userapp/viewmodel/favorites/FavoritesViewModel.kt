package com.userapp.viewmodel.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.userapp.data.ProductRepository
import com.userapp.database.dao.ProductDao
import com.userapp.database.entities.FavoriteProductEntity
import com.userapp.model.ProductItem
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

    private val _favorites = MutableStateFlow<List<ProductItem>>(emptyList())
    val favorites: StateFlow<List<ProductItem>> = _favorites

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    fun checkIfFavorite(productId: String) {
        viewModelScope.launch {
            _isFavorite.value = dao.getFavoriteProducts().any { it.productId == productId }
        }
    }

    fun getFavorites() {
        viewModelScope.launch {
            val favoriteEntities = dao.getFavoriteProducts()
            val ids = favoriteEntities.map { it.productId }
            if (ids.isNotEmpty()) {
                val products = repository.getProductsBatch(ids)
                _favorites.value = products
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
