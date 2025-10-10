package com.userapp.view.components.product

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.userapp.model.ProductItem
import com.userapp.view.components.profile.StoreProfileComponent
import com.userapp.viewmodel.profile.StoreProfileViewModel
import com.userapp.viewmodel.uistate.UiState

@Composable
fun StaggeredProductGrid(
    products: List<ProductItem>,
    modifier: Modifier = Modifier,
    columns: Int = 2,
    onProductClick: (String) -> Unit,
    profile: Boolean = true,
) {
    val storeViewModel: StoreProfileViewModel = hiltViewModel()
    val storeUiState by storeViewModel.storeState.collectAsState()

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(columns),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp
    ) {
        if (profile) {
            when (storeUiState) {
                is UiState.Loading -> {}

                is UiState.Success -> {
                    val store = (storeUiState as UiState.Success).data
                    item(span = StaggeredGridItemSpan.FullLine) {
                        StoreProfileComponent(store)
                    }
                }

                is UiState.Error -> {}

            }
        }

        items(products) { product ->
            ProductCard(
                modifier = modifier.clickable { onProductClick(product.id) },
                product = product,
            )
        }
    }
}
