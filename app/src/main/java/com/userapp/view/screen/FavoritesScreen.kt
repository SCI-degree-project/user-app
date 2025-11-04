package com.userapp.view.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.userapp.view.components.navbar.BottomNavigationBar
import com.userapp.view.components.product.StaggeredProductGrid
import com.userapp.view.components.navbar.TopBar
import com.userapp.viewmodel.favorites.FavoritesViewModel
import com.userapp.viewmodel.uistate.UiState

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onProductClick: (String) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.favoritesState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getFavorites()
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Favorites",
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
                        Text(text = "Error fetching favorites...")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.getFavorites() }) {
                            Text("Retry")
                        }
                    }
                }
                is UiState.Success -> {
                    val favorites = (uiState as UiState.Success).data
                    if (favorites.isEmpty()) {
                        Text(
                            text = "You don't have any favorite products yet.",
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Gray,
                            fontSize = 18.sp
                        )
                    } else {
                        StaggeredProductGrid(
                            products = favorites,
                            modifier = modifier,
                            onProductClick = onProductClick,
                            profile = false
                        )
                    }
                }
            }
        }
    }
}
