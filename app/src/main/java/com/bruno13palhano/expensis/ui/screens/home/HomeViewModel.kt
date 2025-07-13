package com.bruno13palhano.expensis.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.repository.ExpenseRepository
import com.bruno13palhano.expensis.ui.shared.Container
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    initialState: HomeState,
) : ViewModel() {
    val container: Container<HomeState, HomeSideEffect> = Container(
        initialSTATE = initialState,
        scope = viewModelScope,
    )

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.GetExpenses -> getExpenses()
            is HomeEvent.NavigateToExpense -> container.intent {
                postSideEffect(effect = HomeSideEffect.NavigateToExpense(id = event.id))
            }
        }
    }

    private fun getExpenses() = container.intent {
        expenseRepository.getAll().collect { expenses ->
            reduce { copy(expenses = expenses) }
        }
    }
}
