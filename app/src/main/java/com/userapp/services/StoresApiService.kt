package com.userapp.services

import com.userapp.model.StoreDetails
import retrofit2.http.GET
import retrofit2.http.Path

interface StoresApiService {
    @GET("api/stores/{storesId}")
    suspend fun getProductById(
        @Path("storesId") storesId: String
    ): StoreDetails
}