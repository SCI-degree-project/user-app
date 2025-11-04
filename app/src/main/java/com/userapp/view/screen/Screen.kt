package com.userapp.view.screen

sealed class Screen(val route: String) {
    object Home : Screen("home")

    object Store : Screen("store/{storeId}") {
        fun createRoute(storeId: String) = "store/$storeId"
    }

    object ProductDetail : Screen("productDetail/{productId}") {
        fun createRoute(productId: String) = "productDetail/$productId"
    }

    object ARScreen : Screen("productARView/{productId}") {
        fun createRoute(productId: String) = "productARView/$productId"
    }

    object Model3DScreen : Screen("product3DView/{productId}") {
        fun createRoute(productId: String) = "product3DView/$productId"
    }

    object Favorites : Screen("favorites")

    object Search : Screen("search")
}
