package com.bruno13palhano.expensis.ui.screens.home

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.expensis.R
import com.bruno13palhano.expensis.ui.shared.rememberFlowWithLifecycle
import com.bruno13palhano.expensis.ui.theme.ExpensisTheme
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navigateToNewExpense: () -> Unit,
    navigateToExpenses: () -> Unit,
    navigateToAnalytics: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.container.state.collectAsStateWithLifecycle()
    val sideEffect = rememberFlowWithLifecycle(flow = viewModel.container.sideEffect)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val recognizedText = matches?.firstOrNull()

            viewModel.onEvent(event = HomeEvent.ProcessRecognizedText(recognizedText = recognizedText))
        }
    }

    fun startSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, java.util.Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Say the command")
        }
        speechLauncher.launch(intent)
    }

    LaunchedEffect(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                HomeSideEffect.NavigateToNewExpense -> navigateToNewExpense()
                is HomeSideEffect.NavigateToExpenses -> navigateToExpenses()
                HomeSideEffect.NavigateToAnalytics -> navigateToAnalytics()
                HomeSideEffect.StartVoiceRecognition -> startSpeechRecognition()
                HomeSideEffect.ShowError -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Don't get it",
                            withDismissAction = true,
                        )
                    }
                }
                is HomeSideEffect.Command -> {
                    when (effect.command) {
                        HomeCommand.NEW_EXPENSE -> navigateToNewExpense()
                        HomeCommand.EXPENSES -> navigateToExpenses()
                        HomeCommand.ANALYTICS -> navigateToAnalytics()
                        HomeCommand.ERROR -> {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Unknown command",
                                    withDismissAction = true,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    HomeContent(
        snackbarHostState = snackbarHostState,
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    snackbarHostState: SnackbarHostState,
    state: HomeState,
    onEvent: (event: HomeEvent) -> Unit,
) {
    Scaffold(
        modifier = Modifier.consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(HomeEvent.VoiceCommand) }) {
                Icon(
                    imageVector = Icons.Filled.Mic,
                    contentDescription = null,
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) {
        Column(modifier = Modifier.padding(it)) {

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomePreview() {
    ExpensisTheme {
        HomeContent(
            snackbarHostState = SnackbarHostState(),
            state = HomeState(
                profit = 1000.0,
                isProfitVisible = true,
            ),
            onEvent = {},
        )
    }
}
