package com.bruno13palhano.expensis

import app.cash.turbine.test
import com.bruno13palhano.core.data.repository.ExpenseRepository
import com.bruno13palhano.core.model.Expense
import com.bruno13palhano.expensis.ui.screens.expenses.ExpenseEvent
import com.bruno13palhano.expensis.ui.screens.expenses.ExpenseState
import com.bruno13palhano.expensis.ui.screens.expenses.ExpenseViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExpenseViewModelTest {
    private lateinit var viewModel: ExpenseViewModel

    @MockK
    lateinit var expenseRepository: ExpenseRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = ExpenseViewModel(expenseRepository = expenseRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Initial state should have defaults`() = runTest {
        val state = viewModel.container.state.value
        val expected = ExpenseState()
        assertThat(state).isEqualTo(expected)
    }

    @Test
    fun `GetExpense should update state correctly`() = runTest {
        val expense = Expense(
            id = 1L,
            description = "",
            amount = 100.0,
            isIncome = true,
            date = 0L,
            activity = null,
        )
        coEvery { expenseRepository.getById(id = 1L) } returns expense

        viewModel.container.state.test {
            skipItems(1)
            viewModel.onEvent(event = ExpenseEvent.GetExpense(id = 1L))
            val state = awaitItem()
            assertThat(state.id).isEqualTo(expense.id)
            assertThat(state.description).isEqualTo(expense.description)
            assertThat(state.amount).isEqualTo(expense.amount.toString())
            assertThat(state.isIncome).isEqualTo(expense.isIncome)
            assertThat(state.dateInMillis).isEqualTo(expense.date)
            assertThat(state.activity).isEqualTo(expense.activity)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
