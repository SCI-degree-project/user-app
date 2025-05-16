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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.userapp.R
import com.userapp.viewmodel.ProductDetailsViewModelFactoryProvider
import dagger.hilt.android.EntryPointAccessors

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

    val product by viewModel.product.collectAsState()
    var showFullScreenGallery by remember { mutableStateOf(false) }
    var currentFullScreenImageIndex by remember { mutableIntStateOf(0) }

    val safeProduct = product ?: return Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }

    val gallery = safeProduct.gallery.ifEmpty {
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
                text = safeProduct.name.ifBlank { "No name product" },
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$${safeProduct.price}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        ImageCarousel(
            images = gallery,
            productName = safeProduct.name,
            onImageClick = { index ->
                currentFullScreenImageIndex = index
                showFullScreenGallery = true
            }
        )

        Column {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { on3DButtonClick(productId) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_360_24),
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
                        painter = painterResource(id = R.drawable.baseline_view_in_ar_24),
                        contentDescription = "View with AR",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = "See with AR")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = safeProduct.description.ifBlank { "No description" }, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Style", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = safeProduct.style.ifBlank { "Undefined" }, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Materials", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (safeProduct.materials.isNotEmpty()) {
                    safeProduct.materials.joinToString(", ")
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
            productName = safeProduct.name,
            onDismiss = { showFullScreenGallery = false }
        )
    }
}
