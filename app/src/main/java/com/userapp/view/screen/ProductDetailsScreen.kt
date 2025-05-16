package com.userapp.view.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.userapp.view.components.FullscreenImageViewer
import com.userapp.view.components.ImageCarousel
import com.userapp.viewmodel.ProductDetailsViewModel
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.platform.LocalContext
import com.userapp.viewmodel.ProductDetailsViewModelFactoryProvider
import com.userapp.viewmodel.uistate.UiState
import dagger.hilt.android.EntryPointAccessors
import com.userapp.model.ProductDetails
import com.userapp.view.components.ProductActions

@Composable
fun ProductDetailScreen(
    productId: String,
    onARButtonClick: (String) -> Unit,
    on3DButtonClick: (String) -> Unit
) {
    val context = LocalContext.current

    val factory = remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            ProductDetailsViewModelFactoryProvider::class.java
        ).productDetailsViewModelFactory()
    }

    val viewModel: ProductDetailsViewModel = remember {
        factory.create("3fa85f64-5717-4562-b3fc-2c963f66afa6", productId)
    }

    val productState by viewModel.productState.collectAsState()

    when (val state = productState) {
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is UiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.fetchProductDetails() }) {
                        Text("Retry")
                    }
                }
            }
        }

        is UiState.Success -> {
            ProductDetailContent(
                product = state.data,
                onARButtonClick = onARButtonClick,
                on3DButtonClick = on3DButtonClick
            )
        }
    }
}

@Composable
fun ProductDetailContent(
    product: ProductDetails,
    onARButtonClick: (String) -> Unit,
    on3DButtonClick: (String) -> Unit
) {
    var showFullScreenGallery by remember { mutableStateOf(false) }
    var currentFullScreenImageIndex by remember { mutableIntStateOf(0) }

    val gallery = product.gallery.ifEmpty {
        listOf("https://via.placeholder.com/600x400?text=No+Image")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .statusBarsPadding()
    ) {
        Column {
            Text(
                text = product.name.ifBlank { "No name product" },
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        ImageCarousel(
            images = gallery,
            productName = product.name,
            onImageClick = { index ->
                currentFullScreenImageIndex = index
                showFullScreenGallery = true
            }
        )

        Column {
            Spacer(modifier = Modifier.height(12.dp))

            ProductActions(
                productId = product.id,
                modelUrl = product.model,
                on3DButtonClick = on3DButtonClick,
                onARButtonClick = onARButtonClick
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = product.description.ifBlank { "No description" }, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Style", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = product.style.ifBlank { "Undefined" }, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Materials", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (product.materials.isNotEmpty()) {
                    product.materials.joinToString(", ")
                } else {
                    "Undefined"
                },
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

    AnimatedVisibility(
        visible = showFullScreenGallery,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        FullscreenImageViewer(
            images = gallery,
            initialPage = currentFullScreenImageIndex,
            productName = product.name,
            onDismiss = { showFullScreenGallery = false }
        )
    }
}
