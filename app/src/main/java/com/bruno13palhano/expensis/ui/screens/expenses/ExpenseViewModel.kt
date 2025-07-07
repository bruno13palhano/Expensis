package com.bruno13palhano.expensis.ui.screens.expenses

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.repository.ExpenseRepository
import com.bruno13palhano.core.model.Expense
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {
    private var categories = MutableStateFlow(emptyList<Category>())
    private var currentCategory = Category(0L, "")
    var label by mutableStateOf("")
        private set
    var amount by mutableStateOf("")
        private set
    var category by mutableStateOf("")
        private set
    var dateInMillis by mutableLongStateOf(0L)
        private set
    var date by mutableStateOf("")
        private set
    var datePickerVisibility by mutableStateOf(false)
        private set

    fun updateLabel(label: String) {
        this.label = label
    }

    fun updateAmount(amount: String) {
        this.amount = amount
    }

    fun updateCategory(category: String) {
        this.category = category
    }

    fun selectCategory(id: Long) {
        categories.value.firstOrNull { it.id == id }?.let {
            currentCategory = it
        }
    }

    fun showDatePickerDialog(show: Boolean) {
        datePickerVisibility = show
    }

    fun updateDate(dateInMillis: Long, date: String) {
        this.dateInMillis = dateInMillis
        this.date = date
    }

    fun getCategories() {
        viewModelScope.launch {
            categoryRepository.getAll().collect { categoryList ->
                categories.update { categoryList }
            }
        }
    }

    private fun getCategory(id: Long) {
        viewModelScope.launch {
            categoryRepository.getById(id = id)?.let {
                currentCategory = it
            }
        }
    }

    fun getExpense(id: Long) {
        viewModelScope.launch {
            expenseRepository.getById(id)?.let {
                label = it.description
                amount = it.amount.toString()
                dateInMillis = it.date
            }
        }
    }

    fun saveExpense(id: Long) {
        viewModelScope.launch {
            if (id == 0L) {
                val categoryId = categoryRepository.insert(category = currentCategory)

                expenseRepository.insert(
                    expense = Expense(
                        id = id,
                        description = label,
                        amount = amount.toDouble(),
                        isIncome = false,
                        date = dateInMillis,
                        activity = null,
                    ),
                )
            } else {
                expenseRepository.update(
                    expense = Expense(
                        id = id,
                        description = label,
                        amount = amount.toDouble(),
                        isIncome = false,
                        date = dateInMillis,
                        activity = null,
                    ),
                )
            }
        }
    }
}
