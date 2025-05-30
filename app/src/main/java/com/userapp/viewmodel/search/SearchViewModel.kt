package com.userapp.viewmodel.search

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
class SearchViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _searchResults = MutableStateFlow<UiState<List<ProductItem>>>(UiState.Success(emptyList()))
    val searchResults: StateFlow<UiState<List<ProductItem>>> = _searchResults

    fun searchProducts(query: String) {
        viewModelScope.launch {
            _searchResults.value = UiState.Loading
            try {
                val results = repository.searchProducts(query)
                _searchResults.value = UiState.Success(results)
            } catch (e: Exception) {
                _searchResults.value = UiState.Error("Error: ${e.message}")
            }
        }
    }
}
