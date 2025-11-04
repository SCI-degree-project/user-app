package com.userapp.view.components.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.userapp.model.ProductItem

@Composable
fun ProductCard(
    product: ProductItem,
    modifier: Modifier = Modifier,
    customAspectRation: Boolean = true
) {
    val imageUrl = product.cover.orEmpty()

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(if (product.aspectRatio > 0f && customAspectRation) product.aspectRatio else 1f)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop,

                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {

                    }
                },
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No image",
                            color = Color.DarkGray,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.name.ifEmpty { "No name" },
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                )
            }
        }
    }
}
