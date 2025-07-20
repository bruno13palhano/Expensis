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
            AnalyticsEvent.NavigateBack -> container.intent {
                postSideEffect(effect = AnalyticsSideEffect.NavigateBack)
            }
            AnalyticsEvent.UpdateAmount -> container.intent { }
        }
    }
}
