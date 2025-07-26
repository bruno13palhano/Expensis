package com.bruno13palhano.expensis

import app.cash.turbine.test
import com.bruno13palhano.core.data.repository.ExpenseRepository
import com.bruno13palhano.expensis.ui.screens.home.HomeCommand
import com.bruno13palhano.expensis.ui.screens.home.HomeEvent
import com.bruno13palhano.expensis.ui.screens.home.HomeSideEffect
import com.bruno13palhano.expensis.ui.screens.home.HomeViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
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
class HomeViewModelTest {
    private lateinit var viewModel: HomeViewModel

    @MockK
    lateinit var expenseRepository: ExpenseRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = HomeViewModel(expenseRepository = expenseRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Initial state should have defaults`() = runTest {
        val state = viewModel.container.state.value
        assertThat(state.profit).isEqualTo(0.0)
        assertThat(state.isProfitVisible).isFalse()
        assertThat(state.isCommandsInfoVisible).isFalse()
    }

    @Test
    fun `NavigateToNewExpense triggers correct side effect`() = runTest {
        viewModel.container.sideEffect.test {
            viewModel.onEvent(event = HomeEvent.NavigateToNewExpense)
            assertThat(awaitItem()).isEqualTo(HomeSideEffect.NavigateToNewExpense)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `NavigateToExpenses triggers correct side effect`() = runTest {
        viewModel.container.sideEffect.test {
            viewModel.onEvent(event = HomeEvent.NavigateToExpenses)
            assertThat(awaitItem()).isEqualTo(HomeSideEffect.NavigateToExpenses)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `NavigateToAnalytics triggers correct side effect`() = runTest {
        viewModel.container.sideEffect.test {
            viewModel.onEvent(event = HomeEvent.NavigateToAnalytics)
            assertThat(awaitItem()).isEqualTo(HomeSideEffect.NavigateToAnalytics)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `VoiceCommand event triggers StartVoiceRecognition`() = runTest {
        viewModel.container.sideEffect.test {
            viewModel.onEvent(event = HomeEvent.VoiceCommand)
            assertThat(awaitItem()).isEqualTo(HomeSideEffect.StartVoiceRecognition)
        }
    }

    @Test
    fun `ProcessRecognizedText with null text triggers error side effect`() = runTest {
        viewModel.container.sideEffect.test {
            viewModel.onEvent(
                event = HomeEvent.ProcessRecognizedText(
                    recognizedText = null,
                    command = HomeCommand.ANALYTICS,
                ),
            )

            assertThat(awaitItem()).isEqualTo(HomeSideEffect.ShowError)
        }
    }

    @Test
    fun `ProcessRecognizedText updates state and triggers side effect`() = runTest {
        viewModel.container.sideEffect.test {
            viewModel.container.state.test {
                skipItems(1)
                viewModel.onEvent(
                    event = HomeEvent.ProcessRecognizedText(
                        recognizedText = "Analytics",
                        command = HomeCommand.ANALYTICS,
                    ),
                )

                assertThat(awaitItem().recognizedText).isEqualTo("Analytics")
            }

            assertThat(
                awaitItem(),
            ).isEqualTo(HomeSideEffect.NavigateTo(command = HomeCommand.ANALYTICS))
        }
    }

    @Test
    fun `Unrecognized command triggers error side effect`() = runTest {
        viewModel.container.sideEffect.test {
            viewModel.onEvent(
                event = HomeEvent.ProcessRecognizedText(
                    recognizedText = "go somewhere",
                    command = HomeCommand.ERROR,
                ),
            )

            assertThat(
                awaitItem(),
            ).isEqualTo(HomeSideEffect.NavigateTo(command = HomeCommand.ERROR))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ToggleProfitVisibility should toggle state correctly`() = runTest {
        viewModel.container.state.test {
            assertThat(awaitItem().isProfitVisible).isFalse()

            viewModel.onEvent(event = HomeEvent.ToggleProfitVisibility)
            assertThat(awaitItem().isProfitVisible).isTrue()

            viewModel.onEvent(event = HomeEvent.ToggleProfitVisibility)
            assertThat(awaitItem().isProfitVisible).isFalse()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ToggleCommandsVisibility should toggle state correctly`() = runTest {
        viewModel.container.state.test {
            assertThat(awaitItem().isCommandsInfoVisible).isFalse()

            viewModel.onEvent(event = HomeEvent.ToggleCommandsVisibility)
            assertThat(awaitItem().isCommandsInfoVisible).isTrue()

            viewModel.onEvent(event = HomeEvent.ToggleCommandsVisibility)
            assertThat(awaitItem().isCommandsInfoVisible).isFalse()

            cancelAndIgnoreRemainingEvents()
        }
    }
}
