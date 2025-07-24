package com.bruno13palhano.expensis.ui.screens.analytics

import androidx.compose.runtime.Immutable

@Immutable
data class AnalyticsState(
    val amount: Double = 0.0,
    val profit: Double = 0.0,
    val debit: Double = 0.0,
    val chartEntries: List<List<Float>> = emptyList(),
    val isMenuVisible : Boolean = false,
)

@Immutable
sealed interface AnalyticsEvent {
    data class UpdateAnalytics(val groupBy: AnalyticsGroupBy) : AnalyticsEvent
    data object ToggleMenu : AnalyticsEvent
    data class MenuItemSelected(val index: Int) : AnalyticsEvent
    data object NavigateBack : AnalyticsEvent
}

@Immutable
sealed interface AnalyticsSideEffect {
    data object NavigateBack : AnalyticsSideEffect
}

enum class AnalyticsGroupBy { MONTH, WEEK }
