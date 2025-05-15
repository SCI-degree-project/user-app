package com.userapp.view.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.userapp.R
import com.userapp.view.components.ProductCard
import com.userapp.viewmodel.CatalogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    modifier: Modifier = Modifier,
    onProductClick: (String) -> Unit
) {
    val viewModel: CatalogViewModel = hiltViewModel()
    val products by viewModel.products.collectAsState()

    val errorMessage by viewModel.errorMessage.collectAsState()
    if (errorMessage != null) {
        Text(text = errorMessage ?: "", color = Color.Red)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .statusBarsPadding(),
                title = {
                    Text(
                        text = "Catalog",
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                },
                actions = {
                    IconButton(onClick = { /* TODO: Search */ }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_search_24),
                            contentDescription = "Search"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(products, key = { it.id }) { product ->
                    ProductCard(
                        product = product,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { onProductClick(product.id) }
                    )
                }
            }
        }
    }
}