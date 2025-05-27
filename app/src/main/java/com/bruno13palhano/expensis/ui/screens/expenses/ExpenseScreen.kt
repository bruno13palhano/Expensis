package com.bruno13palhano.expensis.ui.screens.expenses

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ExpenseScreen(navigateBack: () -> Unit, viewModel: ExpenseViewModel = viewModel()) {
    ExpenseContent(navigateBack = navigateBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpenseContent(navigateBack: () -> Unit) {
    Scaffold(
        modifier = Modifier.consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = { Text("Expense") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {

        }
    }
}