package com.userapp.data

import com.userapp.model.ProductDetails
import com.userapp.model.ProductItem
import com.userapp.services.ProductsApiService
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val apiService: ProductsApiService
) {
    suspend fun getProducts(tenantId: String, page: Int, size: Int): List<ProductItem> {
        return apiService.getMovies(tenantId, page, size).content
    }

    suspend fun getProductById(tenantId: String, productId: String): ProductDetails {
        return apiService.getProductById(tenantId, productId)
    }

}