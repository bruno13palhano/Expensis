package com.bruno13palhano.expensis.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.bruno13palhano.expensis.ui.screens.expenses.ExpenseScreen
import com.bruno13palhano.expensis.ui.screens.home.HomeScreen
import kotlinx.serialization.Serializable

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(Home)

    NavDisplay(
        modifier = modifier,
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
    ) { key ->
        when (key) {
            Home -> NavEntry(key) {
                HomeScreen(
                    navigateToNewExpense = { backStack.add(Expense(id = 0L)) },
                    navigateToExpense = { backStack.add(Expense(id = it)) },
                )
            }

            is Expense -> NavEntry(key) {
                ExpenseScreen(navigateBack = { backStack.removeLastOrNull() })
            }

            else -> {
                error("Unknown route: $key")
            }
        }
    }
}

@Serializable
data object Home : NavKey

@Serializable
data class Expense(val id: Long) : NavKey
