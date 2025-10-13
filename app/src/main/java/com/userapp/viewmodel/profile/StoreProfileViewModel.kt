package com.userapp.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.userapp.data.StoreRepository
import com.userapp.model.StoreDetails
import com.userapp.viewmodel.uistate.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreProfileViewModel @Inject constructor(
    private val repository: StoreRepository
) : ViewModel() {
    private val tenantId = "3fa85f64-5717-4562-b3fc-2c963f66afa6"

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
}
