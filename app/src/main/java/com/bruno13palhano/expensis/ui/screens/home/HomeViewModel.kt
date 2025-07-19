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
) : ViewModel() {
    val container: Container<HomeState, HomeSideEffect> = Container(
        initialSTATE = HomeState(),
        scope = viewModelScope,
    )

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.NavigateToAnalytics -> container.intent {
                postSideEffect(effect = HomeSideEffect.NavigateToAnalytics)
            }
            HomeEvent.NavigateToExpenses -> container.intent {
                postSideEffect(effect = HomeSideEffect.NavigateToExpenses)
            }
            HomeEvent.NavigateToNewExpense -> container.intent {
                postSideEffect(effect = HomeSideEffect.NavigateToNewExpense)
            }
            HomeEvent.ToggleProfitVisibility -> container.intent {
                reduce { copy(isProfitVisible = !isProfitVisible) }
            }
            HomeEvent.ToggleCommandsVisibility -> container.intent {
                reduce { copy(isCommandsInfoVisible = !isCommandsInfoVisible) }
            }
            is HomeEvent.ProcessRecognizedText -> container.intent {
                if (event.recognizedText == null) {
                    postSideEffect(effect = HomeSideEffect.ShowError)
                } else {
                    reduce { copy(recognizedText = event.recognizedText) }
                    postSideEffect(effect = HomeSideEffect.NavigateTo(command = event.command))
                }
            }
            HomeEvent.VoiceCommand -> container.intent {
                postSideEffect(effect = HomeSideEffect.StartVoiceRecognition)
            }
        }
    }
}
