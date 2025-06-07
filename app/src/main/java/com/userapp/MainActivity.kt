package com.userapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.userapp.view.screen.ARScreen
import com.userapp.view.screen.CatalogScreen
import com.userapp.view.screen.FavoritesScreen
import com.userapp.view.screen.Model3DScreen
import com.userapp.view.screen.ProductDetailScreen
import com.userapp.view.screen.Screen
import com.userapp.view.screen.SearchScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = Screen.Catalog.route) {
                composable(Screen.Catalog.route) {
                    CatalogScreen(
                        navController = navController,
                        onProductClick = { productId ->
                        navController.navigate(Screen.ProductDetail.createRoute(productId))
                        }
                    )
                }

                composable(
                    route = Screen.ProductDetail.route,
                    arguments = listOf(navArgument("productId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    ProductDetailScreen(
                        productId,
                        navController,
                        onARButtonClick = { pId ->
                            navController.navigate(Screen.ARScreen.createRoute(pId))
                        },
                        on3DButtonClick = { pId ->
                            navController.navigate(Screen.Model3DScreen.createRoute(pId))
                        }
                    )
                }

                composable(
                    route = Screen.ARScreen.route,
                    arguments = listOf(navArgument("productId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    ARScreen(productId, navController = navController)
                }

                composable(
                    route = Screen.Model3DScreen.route,
                    arguments = listOf(navArgument("productId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    Model3DScreen(
                        productId,
                        navController = navController,
                    )
                }

                composable(Screen.Favorites.route) {
                    FavoritesScreen(
                        navController = navController,
                        onProductClick = { productId ->
                            navController.navigate(Screen.ProductDetail.createRoute(productId))
                        }
                    )
                }

                composable(Screen.Search.route) {
                    SearchScreen(
                        navController = navController,
                        onProductClick = { productId ->
                            navController.navigate(Screen.ProductDetail.createRoute(productId))
                        }
                    )
                }
            }
        }

    }
}
