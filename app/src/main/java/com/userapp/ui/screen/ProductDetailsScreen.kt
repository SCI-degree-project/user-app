package com.userapp.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.userapp.ui.components.FullscreenImageViewer
import com.userapp.ui.components.ImageCarousel
import com.userapp.viewmodel.ProductDetailsViewModel
import com.userapp.viewmodel.ProductDetailsViewModelFactory
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon

@Composable
fun ProductDetailScreen(
    productId: String,
    onARButtonClick: (String) -> Unit,
    on3DButtonClick: (String) -> Unit
) {
    val viewModel: ProductDetailsViewModel = viewModel(
        factory = ProductDetailsViewModelFactory(productId = productId)
    )
    val product by viewModel.product.collectAsState()
    var showFullScreenGallery by remember { mutableStateOf(false) }
    var currentFullScreenImageIndex by remember { mutableStateOf(0) }

    if (product == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Column(
            ) {
                Text(text = product!!.name, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "$${product!!.price}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }

            ImageCarousel(
                images = product!!.gallery,
                productName = product!!.name,
                onImageClick = { index ->
                    currentFullScreenImageIndex = index
                    showFullScreenGallery = true
                }
            )

            Column(
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { on3DButtonClick(productId) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "View in 3D",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(text = "See in 3D")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { onARButtonClick(productId) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "View with AR",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(text = "See with AR")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = product!!.description, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Style", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = product!!.style, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Materials", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = product!!.materials, style = MaterialTheme.typography.bodyLarge)
            }
        }

        AnimatedVisibility(
            visible = showFullScreenGallery,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            FullscreenImageViewer(
                images = product!!.gallery,
                initialPage = currentFullScreenImageIndex,
                productName = product!!.name,
                onDismiss = { showFullScreenGallery = false }
            )
        }
    }
}
