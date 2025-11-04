package com.userapp.data

import com.userapp.model.Material
import com.userapp.model.PaginatedResponse
import com.userapp.model.ProductDetails
import com.userapp.model.ProductItem
import com.userapp.model.ProductSearchCriteria
import com.userapp.model.Style
import com.userapp.services.ProductsApiService
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val apiService: ProductsApiService
) {
    suspend fun getProducts(tenantId: String, page: Int, size: Int): PaginatedResponse<ProductItem> {
        return apiService.getProducts(tenantId, page, size)
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

    suspend fun getProductsByMaterial(tenantId: String, material: Material, page: Int, size: Int): PaginatedResponse<ProductItem> {
        return apiService.getProductsByMaterial(tenantId, material, page, size)
    }

    suspend fun getProductsByStyle(tenantId: String, style: Style, page: Int, size: Int): PaginatedResponse<ProductItem> {
        return apiService.getProductsByStyle(tenantId, style, page, size)
    }

    suspend fun getScoredProducts(page: Int, size: Int): PaginatedResponse<ProductItem> {
        return apiService.getScoredProducts(page, size)
    }
}
