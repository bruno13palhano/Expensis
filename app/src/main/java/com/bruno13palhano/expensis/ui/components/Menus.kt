package com.bruno13palhano.expensis.ui.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun <T> MoreVertMenu(
    items: Map<T, String>,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onItemClick: (T) -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                text = { Text(text = item.value) },
                onClick = {
                    onItemClick(item.key)
                    onDismissRequest()
                },
            )
        }
    }
}
