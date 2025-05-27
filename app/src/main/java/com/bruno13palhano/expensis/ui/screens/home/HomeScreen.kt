package com.bruno13palhano.expensis.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bruno13palhano.expensis.R

@Composable
fun HomeScreen(navigateToExpense: (Long) -> Unit, viewModel: HomeViewModel = viewModel()) {
    HomeContent(navigateToExpense = navigateToExpense)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(navigateToExpense: (id: Long) -> Unit) {
    Scaffold(
        modifier = Modifier.consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            Button(onClick = { navigateToExpense(1L) }) { Text(text = "Navigate to Expense") }
        }
//        LazyColumn(contentPadding = it) {

//        }
    }
}