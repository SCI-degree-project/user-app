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
import com.userapp.ui.screen.ARScreen
import com.userapp.ui.screen.CatalogScreen
import com.userapp.ui.screen.Model3DScreen
import com.userapp.ui.screen.ProductDetailScreen
import com.userapp.ui.screen.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = Screen.Catalog.route) {
                composable(Screen.Catalog.route) {
                    CatalogScreen(onProductClick = { productId ->
                        navController.navigate(Screen.ProductDetail.createRoute(productId))
                    })
                }

                composable(
                    route = Screen.ProductDetail.route,
                    arguments = listOf(navArgument("productId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    ProductDetailScreen(
                        productId,
                        onARButtonClick = { productId ->
                            navController.navigate(Screen.ARScreen.createRoute(productId))
                        },
                        on3DButtonClick = { productId ->
                            navController.navigate(Screen.Model3DScreen.createRoute(productId))
                        }
                    )
                }

                composable(
                    route = Screen.ARScreen.route,
                    arguments = listOf(navArgument("productId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    ARScreen(productId)
                }

                composable(
                    route = Screen.Model3DScreen.route,
                    arguments = listOf(navArgument("productId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    Model3DScreen(productId)
                }
            }
        }

    }
}
