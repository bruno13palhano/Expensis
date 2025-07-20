package com.bruno13palhano.expensis.ui.screens.analytics

import androidx.compose.runtime.Immutable

@Immutable
data class AnalyticsState(
    val amount: Double = 0.0,
)

@Immutable
sealed interface AnalyticsEvent {
    data object UpdateAmount : AnalyticsEvent
    data object NavigateBack : AnalyticsEvent
}

@Immutable
sealed interface AnalyticsSideEffect {
    data object NavigateBack : AnalyticsSideEffect
}
