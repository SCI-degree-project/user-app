package com.userapp.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.userapp.data.StoreRepository
import com.userapp.model.StoreDetails
import com.userapp.viewmodel.uistate.UiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoreProfileViewModel @AssistedInject constructor(
    private val repository: StoreRepository,
    @Assisted("tenantId") private val tenantId: String,
) : ViewModel() {
    private val _storeState = MutableStateFlow<UiState<StoreDetails>>(UiState.Loading)
    val storeState: StateFlow<UiState<StoreDetails>> = _storeState

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            _storeState.value = UiState.Loading
            try {
                val store = repository.getStoreById(tenantId)
                _storeState.value = UiState.Success(store)
            } catch (e: Exception) {
                _storeState.value = UiState.Error("Error loading store data: ${e.message ?: "Unknown error"}")
                e.printStackTrace()
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("tenantId") tenantId: String
        ): StoreProfileViewModel
    }
}
