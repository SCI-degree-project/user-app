package com.userapp.data

import com.userapp.model.StoreDetails
import com.userapp.services.StoresApiService
import jakarta.inject.Inject

class StoreRepository @Inject constructor(
    private val apiService: StoresApiService
){
    suspend fun getStoreById(storeId: String): StoreDetails {
        return apiService.getProductById(storeId)
    }
}