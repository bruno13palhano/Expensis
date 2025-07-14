package com.bruno13palhano.expensis.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bruno13palhano.core.model.Expense
import com.bruno13palhano.expensis.R
import com.bruno13palhano.expensis.ui.shared.rememberFlowWithLifecycle
import com.bruno13palhano.expensis.ui.theme.ExpensisTheme

@Composable
fun HomeScreen(
    navigateToNewExpense: () -> Unit,
    navigateToExpense: (Long) -> Unit,
    viewModel: HomeViewModel = viewModel(),
) {
    val state by viewModel.container.state.collectAsStateWithLifecycle()
    val sideEffect = rememberFlowWithLifecycle(flow = viewModel.container.sideEffect)

    LaunchedEffect(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                HomeSideEffect.NavigateToNewExpense -> navigateToNewExpense()
                is HomeSideEffect.NavigateToExpense -> navigateToExpense(effect.id)
            }
        }
    }

    HomeContent(state = state, onEvent = viewModel::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(state: HomeState, onEvent: (event: HomeEvent) -> Unit) {
    Scaffold(
        modifier = Modifier.consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(HomeEvent.NavigateToNewExpense) }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                )
            }
        },
    ) {
        LazyColumn(modifier = Modifier.padding(it), contentPadding = PaddingValues(4.dp)) {
            items(items = state.expenses, key = { expense -> expense.id }) { expense ->
                ListItem(
                    modifier = Modifier
                        .clickable { onEvent(HomeEvent.NavigateToExpense(expense.id)) }
                        .padding(4.dp)
                        .fillMaxWidth(),
                    headlineContent = { Text(text = expense.description) },
                    overlineContent = { Text(text = expense.activity ?: "") },
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
            state = HomeState(
                expenses = listOf(
                    Expense(
                        id = 1L,
                        description = "Expense 1",
                        amount = 12.5,
                        isIncome = true,
                        date = 1L,
                        activity = "Activity 1",
                    ),
                    Expense(
                        id = 2L,
                        description = "Expense 2",
                        amount = 12.5,
                        isIncome = false,
                        date = 2L,
                        activity = "Activity 2",
                    ),
                    Expense(
                        id = 3L,
                        description = "Expense 3",
                        amount = 12.5,
                        isIncome = true,
                        date = 3L,
                        activity = "Activity 1",
                    ),
                    Expense(
                        id = 4L,
                        description = "Expense 4",
                        amount = 12.5,
                        isIncome = true,
                        date = 4L,
                        activity = "Activity 1",
                    ),
                    Expense(
                        id = 5L,
                        description = "Expense 5",
                        amount = 12.5,
                        isIncome = false,
                        date = 5L,
                        activity = "Activity 3",
                    ),
                    Expense(
                        id = 6L,
                        description = "Expense 6",
                        amount = 12.5,
                        isIncome = false,
                        date = 6L,
                        activity = "Activity 4",
                    ),
                ),
            ),
            onEvent = {},
        )
    }
}
