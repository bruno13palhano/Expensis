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
import com.bruno13palhano.expensis.ui.screens.analytics.AnalyticsScreen
import com.bruno13palhano.expensis.ui.screens.expenses.ExpenseScreen
import com.bruno13palhano.expensis.ui.screens.expenses.ExpensesScreen
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
            Home -> NavEntry(key) { entry ->
                HomeScreen(
                    navigateToNewExpense = { backStack.add(Expense(id = 0L)) },
                    navigateToExpenses = { backStack.add(Expenses) },
                    navigateToAnalytics = { backStack.add(Analytics) },
                )
            }

            is Expense -> NavEntry(key) {
                ExpenseScreen(
                    id = key.id,
                    navigateBack = { backStack.removeLastOrNull() },
                )
            }

            is Expenses -> NavEntry(key) {
                ExpensesScreen(
                    navigateToNewExpense = { backStack.add(Expense(id = 0L)) },
                    navigateToExpense = { id -> backStack.add(Expense(id = id)) },
                    navigateBack = { backStack.removeLastOrNull() },
                )
            }

            is Analytics -> NavEntry(key) {
                AnalyticsScreen(navigateBack = { backStack.removeLastOrNull() })
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

@Serializable
data object Expenses : NavKey

@Serializable
data object Analytics : NavKey
