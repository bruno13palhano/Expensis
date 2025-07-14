package com.bruno13palhano.expensis.ui.screens.home

import androidx.compose.runtime.Immutable
import com.bruno13palhano.core.model.Expense

@Immutable
data class HomeState(
    val expenses: List<Expense> = emptyList(),
)

@Immutable
sealed interface HomeEvent {
    data object GetExpenses : HomeEvent
    data object NavigateToNewExpense : HomeEvent
    data class NavigateToExpense(val id: Long) : HomeEvent
}

@Immutable
sealed interface HomeSideEffect {
    data object NavigateToNewExpense : HomeSideEffect
    data class NavigateToExpense(val id: Long) : HomeSideEffect
}
