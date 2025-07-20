package com.bruno13palhano.expensis.ui.screens.expenses

import androidx.compose.runtime.Immutable
import com.bruno13palhano.core.model.Expense

@Immutable
data class ExpensesState(
    val expenses: List<Expense> = emptyList(),
)

@Immutable
sealed interface ExpensesEvent {
    data object GetExpenses : ExpensesEvent
    data object NavigateToNewExpense : ExpensesEvent
    data class NavigateToExpense(val id: Long) : ExpensesEvent
    data object NavigateBack : ExpensesEvent
}

@Immutable
sealed interface ExpensesSideEffect {
    data object NavigateToNewExpense : ExpensesSideEffect
    data class NavigateToExpense(val id: Long) : ExpensesSideEffect
    data object NavigateBack : ExpensesSideEffect
}
