package com.userapp.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.userapp.R

@Composable
fun ProductActions(
    productId: String,
    modelUrl: String?,
    on3DButtonClick: (String) -> Unit,
    onARButtonClick: (String) -> Unit
) {
    if (!modelUrl.isNullOrBlank()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
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
    }
}
