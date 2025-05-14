package com.userapp.services

import com.userapp.model.PaginatedResponse
import com.userapp.model.ProductDetails
import com.userapp.model.ProductItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApiService {
    @GET("api/products/{tenantId}")
    suspend fun getMovies(
        @Path("tenantId") tenantId: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): PaginatedResponse<ProductItem>

    @GET("api/products/{tenantId}/{productId}")
    suspend fun getProductById(
        @Path("tenantId") tenantId: String,
        @Path("productId") productId: String
    ): ProductDetails

}
