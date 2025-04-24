package com.userapp.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.userapp.viewmodel.ProductDetailsViewModel
import com.userapp.viewmodel.ProductDetailsViewModelFactory

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetailScreen(productId: String) {
    val viewModel: ProductDetailsViewModel = viewModel(
        factory = ProductDetailsViewModelFactory(productId = productId)
    )
    val product by viewModel.product.collectAsState()

    if (product == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (product!!.gallery.isNotEmpty()) {
                val pagerState = rememberPagerState(initialPage = 0) { product!!.gallery.size }
                HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) { page ->
                    Image(
                        painter = rememberAsyncImagePainter(product!!.gallery[page]),
                        contentDescription = product!!.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.6f),
                        contentScale = ContentScale.Crop
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(product!!.gallery.size) { index ->
                        val color = if (pagerState.currentPage == index) Color.Gray else Color.LightGray
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = product!!.name, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "$${product!!.price}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = product!!.description, style = MaterialTheme.typography.bodyLarge)
            }

        }
    }
}
