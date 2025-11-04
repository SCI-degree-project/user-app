package com.userapp.view.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.userapp.model.Material
import com.userapp.model.ProductItem
import com.userapp.model.Style
import com.userapp.view.components.carousel.ProductCarousel
import com.userapp.view.components.navbar.BottomNavigationBar
import com.userapp.view.components.profile.StoreProfileComponent
import com.userapp.viewmodel.MaterialCarouselViewModelFactoryProvider
import com.userapp.viewmodel.StoreViewModelFactoryProvider
import com.userapp.viewmodel.StyleCarouselViewModelFactoryProvider
import com.userapp.viewmodel.profile.StoreProfileViewModel
import com.userapp.viewmodel.uistate.UiState
import dagger.hilt.android.EntryPointAccessors

@Composable
fun StoreScreen(
    storeId: String,
    navController: NavController,
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val storeFactory = remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            StoreViewModelFactoryProvider::class.java
        ).storeViewModelFactory()
    }

    val storeViewModel: StoreProfileViewModel = remember {
        storeFactory.create(storeId)
    }

    val storeState by storeViewModel.storeState.collectAsState()

    val materialFactory = remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            MaterialCarouselViewModelFactoryProvider::class.java
        ).materialCarouselViewModelFactoryProvider()
    }

    val styleFactory = remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            StyleCarouselViewModelFactoryProvider::class.java
        ).styleCarouselViewModelFactoryProvider()
    }

    val materialViewModels = remember {
        Material.entries.associateWith { material ->
            materialFactory.create(storeId, material, 0, 10)
        }
    }

    val styleViewModels = remember {
        Style.entries.associateWith { style ->
            styleFactory.create(storeId, style, 0, 10)
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (storeState) {
                is UiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Internal Server Error")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { storeViewModel.fetchData() }) {
                            Text("Retry")
                        }
                    }
                }

                is UiState.Success -> {
                    val storeData = (storeState as UiState.Success).data

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        StoreProfileComponent(storeData)
                        Spacer(modifier = Modifier.height(8.dp))

                        styleViewModels.forEach { (style, vm) ->
                            val state by vm.productsState.collectAsState()

                            if (state is UiState.Success) {
                                val products = (state as UiState.Success<List<ProductItem>>).data
                                if (products.isNotEmpty()) {
                                    println("Rendering ${products.size} products for $style")
                                    ProductCarousel(
                                        title = "${style.name.lowercase().replaceFirstChar { it.uppercase() }} Style",
                                        products = products,
                                        onProductClick = onProductClick
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }

                        materialViewModels.forEach { (material, vm) ->
                            val state by vm.productsState.collectAsState()

                            if (state is UiState.Success) {
                                val products = (state as UiState.Success<List<ProductItem>>).data
                                if (products.isNotEmpty()) {
                                    println("Rendering ${products.size} products for $material")
                                    ProductCarousel(
                                        title = "Products made of ${material.name.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }}",
                                        products = products,
                                        onProductClick = onProductClick
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}