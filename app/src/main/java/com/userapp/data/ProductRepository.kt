package com.userapp.data

import com.userapp.model.PaginatedResponse
import com.userapp.model.ProductDetails
import com.userapp.model.ProductItem
import com.userapp.model.ProductSearchCriteria
import com.userapp.services.ProductsApiService
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val apiService: ProductsApiService
) {
    suspend fun getProducts(tenantId: String, page: Int, size: Int): PaginatedResponse<ProductItem> {
        return apiService.getMovies(tenantId, page, size)
    }

    suspend fun getProductById(productId: String): ProductDetails {
        return apiService.getProductById(productId)
    }

    suspend fun getProductsBatch(productIds: List<String>): List<ProductItem> {
        return apiService.getProductsBatch(productIds)
    }

    suspend fun searchProducts(criteria: ProductSearchCriteria): List<ProductItem> {
        return apiService.searchProducts(criteria).content
    }
}
