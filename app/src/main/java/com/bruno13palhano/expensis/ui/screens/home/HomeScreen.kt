package com.bruno13palhano.expensis.ui.screens.home

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Expense
import com.bruno13palhano.expensis.R
import com.bruno13palhano.expensis.ui.theme.ExpensisTheme

@Composable
fun HomeScreen(navigateToExpense: (Long) -> Unit, viewModel: HomeViewModel = viewModel()) {
    HomeContent(expenses = emptyList(), navigateToExpense = navigateToExpense)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(expenses: List<Expense>, navigateToExpense: (id: Long) -> Unit) {
    Scaffold(
        modifier = Modifier.consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
        },
    ) {
        LazyColumn(contentPadding = it) {
            items(items = expenses, key = { expense -> expense.id }) { expense ->
                ListItem(
                    modifier = Modifier.fillMaxWidth(),
                    headlineContent = { Text(text = expense.label) },
                    overlineContent = { Text(text = expense.category.name) },
                    trailingContent = { Text(text = "${expense.amount}") },
                    supportingContent = { Text(text = "${expense.date}") },
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomePreview() {
    ExpensisTheme {
        HomeContent(
            expenses = listOf(
                Expense(
                    id = 1L,
                    label = "Expense 1",
                    amount = 12.5,
                    category = Category(id = 1L, "Category 1"),
                    date = 1L,
                ),
                Expense(
                    id = 2L,
                    label = "Expense 2",
                    amount = 12.5,
                    category = Category(id = 2L, "Category 2"),
                    date = 2L,
                ),
                Expense(
                    id = 3L,
                    label = "Expense 3",
                    amount = 12.5,
                    category = Category(id = 3L, "Category 3"),
                    date = 3L,
                ),
                Expense(
                    id = 4L,
                    label = "Expense 4",
                    amount = 12.5,
                    category = Category(id = 4L, "Category 4"),
                    date = 4L,
                ),
                Expense(
                    id = 5L,
                    label = "Expense 5",
                    amount = 12.5,
                    category = Category(id = 5L, "Category 5"),
                    date = 5L,
                ),
                Expense(
                    id = 6L,
                    label = "Expense 6",
                    amount = 12.5,
                    category = Category(id = 6L, "Category 6"),
                    date = 6L,
                ),
            ),
            navigateToExpense = {},
        )
    }
}
