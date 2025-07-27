package com.bruno13palhano.expensis.ui.screens.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.repository.ExpenseRepository
import com.bruno13palhano.core.model.Expense
import com.bruno13palhano.expensis.ui.shared.Container
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
) : ViewModel() {
    val container: Container<ExpenseState, ExpenseSideEffect> = Container(
        initialSTATE = ExpenseState(),
        scope = viewModelScope,
    )

    fun onEvent(event: ExpenseEvent) {
        when (event) {
            is ExpenseEvent.GetExpense -> getExpense(id = event.id)
            is ExpenseEvent.SaveExpense -> saveExpense()
            is ExpenseEvent.UpdateDescription -> container.intent {
                reduce { copy(description = event.description) }
            }
            is ExpenseEvent.UpdateAmount -> container.intent {
                reduce { copy(amount = event.amount) }
            }
            is ExpenseEvent.ToggleIsIncome -> container.intent {
                reduce { copy(isIncome = !isIncome) }
            }
            is ExpenseEvent.UpdateDate -> container.intent {
                reduce {
                    copy(
                        dateInMillis = event.dateInMillis,
                        date = event.date,
                    )
                }
            }
            is ExpenseEvent.UpdateActivity -> container.intent {
                reduce { copy(activity = event.activity) }
            }
            ExpenseEvent.ToggleDatePickerVisibility -> container.intent {
                reduce { copy(isDatePickerVisible = !isDatePickerVisible) }
            }
            ExpenseEvent.DismissKeyboard -> container.intent {
                postSideEffect(effect = ExpenseSideEffect.DismissKeyboard)
            }
            ExpenseEvent.NavigateBack -> container.intent {
                postSideEffect(effect = ExpenseSideEffect.NavigateBack)
            }
        }
    }

    private fun getExpense(id: Long) = container.intent {
        expenseRepository.getById(id = id)?.let {
            reduce {
                copy(
                    id = id,
                    description = it.description,
                    amount = it.amount.toString(),
                    isIncome = it.isIncome,
                    dateInMillis = it.date,
                    activity = it.activity,
                )
            }
        }
    }

    fun saveExpense() = container.intent {
        val id = container.state.value.id

        if (id == 0L) {
            expenseRepository.insert(
                expense = Expense(
                    id = id,
                    description = container.state.value.description,
                    amount = container.state.value.amount.toDouble(),
                    isIncome = container.state.value.isIncome,
                    date = container.state.value.dateInMillis,
                    activity = container.state.value.activity,
                ),
            )
        } else {
            expenseRepository.update(
                expense = Expense(
                    id = id,
                    description = container.state.value.description,
                    amount = container.state.value.amount.toDouble(),
                    isIncome = container.state.value.isIncome,
                    date = container.state.value.dateInMillis,
                    activity = container.state.value.activity,
                ),
            )
        }

        postSideEffect(effect = ExpenseSideEffect.NavigateBack)
    }
}
