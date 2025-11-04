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
import com.userapp.view.screen.HomeScreen
import com.userapp.view.screen.FavoritesScreen
import com.userapp.view.screen.Model3DScreen
import com.userapp.view.screen.ProductDetailScreen
import com.userapp.view.screen.Screen
import com.userapp.view.screen.SearchScreen
import com.userapp.view.screen.StoreScreen
import com.userapp.view.theme.UserAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UserAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.Home.route) {
                    composable(
                        Screen.Home.route,
                    ) {
                        HomeScreen(
                            navController = navController,
                            onProductClick = { productId ->
                                navController.navigate(Screen.ProductDetail.createRoute(productId))
                            }
                        )
                    }

                    composable(
                        route = Screen.Store.route,
                        arguments = listOf(navArgument("storeId") { type = NavType.StringType})
                    ) { backStackEntry ->
                        val storeId = backStackEntry.arguments?.getString("storeId") ?: ""
                        StoreScreen(
                            storeId,
                            navController,
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
                            },
                            onStoreClick = { sId ->
                                navController.navigate(Screen.Store.createRoute(sId))
                            },
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
}
