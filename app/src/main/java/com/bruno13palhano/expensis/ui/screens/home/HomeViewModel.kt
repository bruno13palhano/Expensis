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
            is HomeEvent.ProcessRecognizedText -> container.intent {
                if (event.recognizedText == null){
                    postSideEffect(effect = HomeSideEffect.ShowError)
                } else {
                    reduce { copy(recognizedText = event.recognizedText) }

                    val command = getHomeCommand(event.recognizedText)
                    postSideEffect(effect = HomeSideEffect.Command(command = command))
                }
            }
            HomeEvent.VoiceCommand -> container.intent {
                postSideEffect(effect = HomeSideEffect.StartVoiceRecognition)
            }
        }
    }

    private fun getHomeCommand(recognizedText: String?): HomeCommand {
        if (recognizedText?.contains(other = "to new expense", ignoreCase = true) == true) {
            return HomeCommand.NEW_EXPENSE
        }
        if (recognizedText?.contains(other = "to expenses", ignoreCase = true) == true) {
            return HomeCommand.EXPENSES
        }
        if (recognizedText?.contains(other = "to analytics", ignoreCase = true) == true) {
            return HomeCommand.ANALYTICS
        }

        return HomeCommand.ERROR
    }
}
