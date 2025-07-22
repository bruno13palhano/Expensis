package com.bruno13palhano.expensis.ui.screens.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.expensis.ui.shared.Container
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor() : ViewModel() {
    val container: Container<AnalyticsState, AnalyticsSideEffect> = Container(
        initialSTATE = AnalyticsState(),
        scope = viewModelScope,
    )

    fun onEvent(event: AnalyticsEvent) {
        when (event) {
            AnalyticsEvent.UpdateChart -> updateChart()
            AnalyticsEvent.NavigateBack -> container.intent {
                postSideEffect(effect = AnalyticsSideEffect.NavigateBack)
            }
            AnalyticsEvent.UpdateAmount -> container.intent { }
        }
    }

    private fun updateChart() = container.intent {
        val chartEntries = listOf(
            listOf(1f, 3f, 7f, 5f, 11f, 9f),
            listOf(4f, 8f, 12f, 2f, 6f, 10f),
        )

        reduce { copy(chartEntries = chartEntries) }
    }
}
