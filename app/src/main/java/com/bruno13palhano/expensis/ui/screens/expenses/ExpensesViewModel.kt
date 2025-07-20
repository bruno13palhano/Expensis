package com.bruno13palhano.expensis.ui.screens.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.repository.ExpenseRepository
import com.bruno13palhano.expensis.ui.shared.Container
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
) : ViewModel() {
    val container: Container<ExpensesState, ExpensesSideEffect> = Container(
        initialSTATE = ExpensesState(),
        scope = viewModelScope,
    )

    fun onEvent(event: ExpensesEvent) {
        when (event) {
            ExpensesEvent.NavigateToNewExpense -> container.intent {
                postSideEffect(effect = ExpensesSideEffect.NavigateToNewExpense)
            }
            ExpensesEvent.GetExpenses -> getExpenses()
            is ExpensesEvent.NavigateToExpense -> container.intent {
                postSideEffect(effect = ExpensesSideEffect.NavigateToExpense(id = event.id))
            }
            ExpensesEvent.NavigateBack -> container.intent {
                postSideEffect(effect = ExpensesSideEffect.NavigateBack)
            }
        }
    }

    private fun getExpenses() = container.intent {
        expenseRepository.getAll().collect { expenses ->
            reduce { copy(expenses = expenses) }
        }
    }
}
