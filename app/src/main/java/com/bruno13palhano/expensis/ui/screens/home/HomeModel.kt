package com.bruno13palhano.expensis.ui.screens.home

import androidx.compose.runtime.Immutable

@Immutable
data class HomeState(
    val profit: Double = 0.0,
    val recognizedText: String = "",
    val isProfitVisible: Boolean = false,
)

@Immutable
sealed interface HomeEvent {
    data object NavigateToNewExpense : HomeEvent
    data object NavigateToExpenses : HomeEvent
    data object NavigateToAnalytics : HomeEvent
    data object VoiceCommand : HomeEvent
    data class ProcessRecognizedText(
        val recognizedText: String?,
        val command: HomeCommand,
    ) : HomeEvent
}

@Immutable
sealed interface HomeSideEffect {
    data object NavigateToNewExpense : HomeSideEffect
    data object NavigateToExpenses : HomeSideEffect
    data object NavigateToAnalytics : HomeSideEffect
    data object StartVoiceRecognition : HomeSideEffect
    data object ShowError : HomeSideEffect
    data class Command(val command: HomeCommand) : HomeSideEffect
}
