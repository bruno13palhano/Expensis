package com.bruno13palhano.expensis.ui.screens.analytics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.expensis.R
import com.bruno13palhano.expensis.ui.shared.rememberFlowWithLifecycle
import com.bruno13palhano.expensis.ui.theme.ExpensisTheme
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries

@Composable
fun AnalyticsScreen(navigateBack: () -> Unit, viewModel: AnalyticsViewModel = hiltViewModel()) {
    val state by viewModel.container.state.collectAsStateWithLifecycle()
    val sideEffect = rememberFlowWithLifecycle(flow = viewModel.container.sideEffect)

    LaunchedEffect(Unit) {
        viewModel.onEvent(event = AnalyticsEvent.UpdateAnalytics(groupBy = AnalyticsGroupBy.MONTH))
    }

    LaunchedEffect(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                AnalyticsSideEffect.NavigateBack -> navigateBack()
            }
        }
    }

    AnalyticsContent(state = state, onEvent = viewModel::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnalyticsContent(state: AnalyticsState, onEvent: (event: AnalyticsEvent) -> Unit) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(state.chartEntries) {
        modelProducer.runTransaction {
            if (state.chartEntries.isNotEmpty()) {
                columnSeries {
                    state.chartEntries.forEach { series(it) }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.analytics)) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(AnalyticsEvent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.navigate_back),
                        )
                    }
                },
            )
        },
    ) {
        Column(modifier = Modifier.padding(it)) {
            ElevatedCard(modifier = Modifier.padding(8.dp)) {
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp, end = 8.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.amount_tag, state.amount),
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp, end = 8.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.profit_tag, state.profit),
                )
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = stringResource(id = R.string.debit_tag, state.debit),
                )
            }
            CartesianChartHost(
                chart = rememberCartesianChart(
                    rememberColumnCartesianLayer(),
                    startAxis = VerticalAxis.rememberStart(),
                    bottomAxis = HorizontalAxis.rememberBottom(),
                ),
                modelProducer = modelProducer,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AnalyticsPreview() {
    ExpensisTheme {
        AnalyticsContent(
            state = AnalyticsState(
                amount = 400.0,
                profit = 300.0,
                debit = 100.0,
            ),
            onEvent = {},
        )
    }
}
