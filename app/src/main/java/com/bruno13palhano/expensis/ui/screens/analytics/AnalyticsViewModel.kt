package com.bruno13palhano.expensis.ui.screens.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.repository.ExpenseRepository
import com.bruno13palhano.core.model.Expense
import com.bruno13palhano.expensis.ui.shared.Container
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val expensesRepository: ExpenseRepository,
) : ViewModel() {
    val container: Container<AnalyticsState, AnalyticsSideEffect> = Container(
        initialSTATE = AnalyticsState(),
        scope = viewModelScope,
    )

    fun onEvent(event: AnalyticsEvent) {
        when (event) {
            is AnalyticsEvent.UpdateAnalytics -> updateChart(groupBy = event.groupBy)
            AnalyticsEvent.NavigateBack -> container.intent {
                postSideEffect(effect = AnalyticsSideEffect.NavigateBack)
            }
        }
    }

    private fun updateChart(groupBy: AnalyticsGroupBy) = container.intent {
        expensesRepository.getAll().collect { expenses ->
            val newState = calculateAnalytics(expenses = expenses, groupBy = groupBy)
            reduce { newState }
        }
    }

    private fun calculateAnalytics(
        expenses: List<Expense>,
        groupBy: AnalyticsGroupBy,
    ): AnalyticsState {
        val dateFormat = when (groupBy) {
            AnalyticsGroupBy.MONTH -> DateTimeFormatter.ofPattern("yyyy-MM")
            AnalyticsGroupBy.WEEK -> DateTimeFormatter.ofPattern("YYYY-'W'ww")
        }

        val groupedExpenses = expenses.groupBy {
            Instant.ofEpochMilli(it.date).atZone(ZoneId.systemDefault()).format(dateFormat)
        }

        val amountList = mutableListOf(0f)
        val debitList = mutableListOf(0f)
        var totalAmount = 0.0
        var totalDebit = 0.0

        groupedExpenses.toSortedMap().values.forEach { group ->
            var groupAmount = 0f
            var groupDebit = 0f

            group.forEach { expense ->
                if (expense.isIncome) {
                    groupAmount += expense.amount.toFloat()
                    totalAmount += expense.amount
                } else {
                    groupDebit += expense.amount.toFloat()
                    totalDebit += expense.amount
                }
            }

            amountList.add(groupAmount)
            debitList.add(groupDebit)
        }

        val profit = totalAmount - totalDebit
        return AnalyticsState(
            amount = totalAmount,
            profit = profit,
            debit = totalDebit,
            chartEntries = listOf(amountList, debitList),
        )
    }
}
