package com.userapp.ui.screen

sealed class Screen(val route: String) {
    object Catalog : Screen("catalog")
    object ProductDetail : Screen("productDetail/{productId}") {
        fun createRoute(productId: String) = "productDetail/$productId"
    }
}
