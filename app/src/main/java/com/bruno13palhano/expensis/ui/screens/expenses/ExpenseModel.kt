package com.bruno13palhano.expensis.ui.screens.expenses

import androidx.compose.runtime.Immutable
import com.bruno13palhano.expensis.ui.shared.ErrorType

@Immutable
data class ExpenseState(
    val id: Long = 0L,
    val description: String = "",
    val amount: String = "",
    val isIncome: Boolean = false,
    val date: String = "",
    val dateInMillis: Long = 0L,
    val activity: String? = null,
    val isDatePickerVisible: Boolean = false,
    val descriptionError: Boolean = false,
    val amountError: Boolean = false,
)

@Immutable
sealed interface ExpenseEvent {
    data class GetExpense(val id: Long) : ExpenseEvent
    data object SaveExpense : ExpenseEvent
    data class UpdateDescription(val description: String) : ExpenseEvent
    data class UpdateAmount(val amount: String) : ExpenseEvent
    data object ToggleIsIncome : ExpenseEvent
    data class UpdateDate(val date: Long) : ExpenseEvent
    data class UpdateActivity(val activity: String?) : ExpenseEvent
    data object ToggleDatePickerVisibility : ExpenseEvent
    data object NavigateBack : ExpenseEvent
}

@Immutable
sealed interface ExpenseSideEffect {
    data class ShowError(val errorType: ErrorType) : ExpenseSideEffect
    data object DismissKeyboard : ExpenseSideEffect
    data object NavigateBack : ExpenseSideEffect
}
