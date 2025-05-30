package com.userapp.view.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.userapp.view.components.BottomNavigationBar
import com.userapp.view.components.ProductCard
import com.userapp.view.components.StaggeredProductGrid
import com.userapp.view.components.TopBar
import com.userapp.viewmodel.catalog.CatalogViewModel
import com.userapp.viewmodel.uistate.UiState

@Composable
fun CatalogScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onProductClick: (String) -> Unit
) {
    val viewModel: CatalogViewModel = hiltViewModel()
    val uiState by viewModel.productsState.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                title = "Catalog",
                modifier = modifier
            )
        },

        bottomBar = {
            BottomNavigationBar(navController)
        }

    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator()
                }
                is UiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Internal Server Error")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.fetchProducts() }) {
                            Text("Retry")
                        }
                    }
                }
                is UiState.Success -> {
                    val products = (uiState as UiState.Success).data
                    StaggeredProductGrid(
                        products = products,
                        modifier = modifier,
                        onProductClick = onProductClick
                    )
                }
            }
        }
    }
}
