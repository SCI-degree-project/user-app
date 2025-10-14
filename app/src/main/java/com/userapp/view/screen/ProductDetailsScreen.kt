package com.userapp.view.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.userapp.view.components.product.FullscreenImageViewer
import com.userapp.view.components.product.ImageCarousel
import com.userapp.viewmodel.product.ProductDetailsViewModel
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.userapp.R
import com.userapp.viewmodel.product.ProductDetailsViewModelFactoryProvider
import com.userapp.viewmodel.uistate.UiState
import dagger.hilt.android.EntryPointAccessors
import com.userapp.model.ProductDetails
import com.userapp.model.StoreDetails
import com.userapp.view.components.product.ProductActions
import com.userapp.view.components.profile.StoreProfilePreview
import com.userapp.viewmodel.favorites.FavoritesViewModel
import com.userapp.viewmodel.profile.StoreProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun ProductDetailScreen(
    productId: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    onARButtonClick: (String) -> Unit,
    on3DButtonClick: (String) -> Unit,
    onStoreClick: (String) -> Unit
) {
    val context = LocalContext.current

    val factory = remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            ProductDetailsViewModelFactoryProvider::class.java
        ).productDetailsViewModelFactory()
    }

    val viewModel: ProductDetailsViewModel = remember {
        factory.create(productId)
    }

    val storeViewModel: StoreProfileViewModel = hiltViewModel()
    val storeUiState by storeViewModel.storeState.collectAsState()

    val productState by viewModel.productState.collectAsState()

    val favoritesViewModel: FavoritesViewModel = hiltViewModel()

    LaunchedEffect(productId) {
        favoritesViewModel.checkIfFavorite(productId)
    }

    val isFavorite by favoritesViewModel.isFavorite.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    var isImageFullScreenVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = !isImageFullScreenVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .statusBarsPadding()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
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
                    val store = (storeUiState as UiState.Success).data
                    ProductDetailContent(
                        product = state.data,
                        modifier = modifier,
                        onARButtonClick = onARButtonClick,
                        on3DButtonClick = on3DButtonClick,
                        isFavorite = isFavorite,
                        onToggleFavorite = {
                            coroutineScope.launch {
                                favoritesViewModel.toggleFavorite(productId)
                            }
                        },
                        store = store,
                        onSetFullScreenVisible = { isVisible ->
                            isImageFullScreenVisible = isVisible
                        },
                        onStoreClick = onStoreClick
                    )
                }
            }
        }
    }
}

@Composable
fun ProductDetailContent(
    product: ProductDetails,
    modifier: Modifier,
    onARButtonClick: (String) -> Unit,
    on3DButtonClick: (String) -> Unit,
    isFavorite: Boolean = false,
    onToggleFavorite: () -> Unit = {},
    store: StoreDetails,
    onSetFullScreenVisible: (Boolean) -> Unit,
    onStoreClick: (String) -> Unit
) {
    var showFullScreenGallery by remember { mutableStateOf(false) }
    var currentFullScreenImageIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(showFullScreenGallery) {
        onSetFullScreenVisible(showFullScreenGallery)
    }

    val gallery = product.media.gallery

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        ) {
            ImageCarousel(
                images = gallery,
                productName = product.name,
                onImageClick = { index ->
                    currentFullScreenImageIndex = index
                    showFullScreenGallery = true
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
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

                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        painter = if (isFavorite)
                            painterResource(id = R.drawable.baseline_bookmark_24)
                        else
                            painterResource(id = R.drawable.rounded_bookmark_24),
                        contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                    )
                }
            }

            Column {
                ProductActions(
                    productId = product.id,
                    modelUrl = product.media.model,
                    on3DButtonClick = on3DButtonClick,
                    onARButtonClick = onARButtonClick
                )

                Spacer(modifier = Modifier.height(12.dp))
                product.description?.let {
                    Text(
                        text = it.ifBlank { "No description" },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Style", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                product.style?.let {
                    Text(
                        text = it.ifBlank { "Undefined" },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
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
                Spacer(modifier = Modifier.height(8.dp))
                if (product.dimensions != null) {
                    Text(
                        text = "Dimensions",
                        style = MaterialTheme.typography.titleMedium
                    )
                    product.dimensions.width.let { width ->
                        Text(
                            text = "\t• Width: $width",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    product.dimensions.height.let { height ->
                        Text(
                            text = "\t• Height: $height",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    product.dimensions.depth.let { depth ->
                        Text(
                            text = "\t• Depth: $depth",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "Store info", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                StoreProfilePreview(modifier = modifier.clickable { onStoreClick(store.id) }, store = store)
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
}
