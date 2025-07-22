package com.bruno13palhano.expensis.ui.screens.analytics

import androidx.compose.runtime.Immutable

@Immutable
data class AnalyticsState(
    val amount: Double = 0.0,
    val profit: Double = 0.0,
    val debit: Double = 0.0,
    val chartEntries: List<List<Float>> = emptyList(),
)

@Immutable
sealed interface AnalyticsEvent {
    data object UpdateChart : AnalyticsEvent
    data object UpdateAnalyticsValues : AnalyticsEvent
    data object NavigateBack : AnalyticsEvent
}

@Immutable
sealed interface AnalyticsSideEffect {
    data object NavigateBack : AnalyticsSideEffect
}
