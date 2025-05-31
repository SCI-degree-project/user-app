package com.userapp.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilterDialog(
    currentSortBy: String,
    currentDirection: String,
    onDismiss: () -> Unit,
    onApply: (String, String) -> Unit
) {
    val options = listOf(
        Triple("Name A-Z", "name", "asc"),
        Triple("Name Z-A", "name", "desc"),
        Triple("Lower Price", "price", "asc"),
        Triple("Higher Price", "price", "desc")
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sort Options") },
        text = {
            Column {
                options.forEach { (label, sortBy, direction) ->
                    FilterOption(
                        label = label,
                        selected = (currentSortBy == sortBy && currentDirection == direction),
                        onClick = { onApply(sortBy, direction) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun FilterOption(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() }
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label)
    }
}