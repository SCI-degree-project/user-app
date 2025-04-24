package com.userapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.userapp.viewmodel.CatalogViewModel

@Composable
fun CatalogScreen(
    modifier: Modifier = Modifier,
    onProductClick: (String) -> Unit
) {
    val viewModel: CatalogViewModel = viewModel()
    val products by viewModel.products.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Catalog",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(12.dp)
        )

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(products) { product ->
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
