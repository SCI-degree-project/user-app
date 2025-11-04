package com.userapp.services

import com.userapp.model.Material
import com.userapp.model.PaginatedResponse
import com.userapp.model.ProductDetails
import com.userapp.model.ProductItem
import com.userapp.model.ProductSearchCriteria
import com.userapp.model.Style
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApiService {
    @GET("api/products/{tenantId}")
    suspend fun getProducts(
        @Path("tenantId") tenantId: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): PaginatedResponse<ProductItem>

    @GET("api/products/id/{productId}")
    suspend fun getProductById(
        @Path("productId") productId: String
    ): ProductDetails

    @POST("api/products/batch")
    suspend fun getProductsBatch(
        @Body ids: List<String>
    ): List<ProductItem>

    @POST("api/products/search")
    suspend fun searchProducts(
        @Body criteria: ProductSearchCriteria
    ): PaginatedResponse<ProductItem>

    @GET("api/products/{tenantId}/material")
    suspend fun getProductsByMaterial(
        @Path("tenantId") tenantId: String,
        @Query("material") material: Material,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): PaginatedResponse<ProductItem>

    @GET("api/products/{tenantId}/style")
    suspend fun getProductsByStyle(
        @Path("tenantId") tenantId: String,
        @Query("style") style: Style,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): PaginatedResponse<ProductItem>

    @GET("api/products/scored")
    suspend fun getScoredProducts(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): PaginatedResponse<ProductItem>
}
