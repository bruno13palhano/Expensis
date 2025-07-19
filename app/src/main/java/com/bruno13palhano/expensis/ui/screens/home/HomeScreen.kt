package com.bruno13palhano.expensis.ui.screens.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    val context = LocalContext.current
    val speechInputDialogMessage = stringResource(id = R.string.say_command)
    val unknownCommandMessage = stringResource(id = R.string.unknown_command)
    val unexpectedErrorMessage = stringResource(id = R.string.unexpected_error)

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val recognizedText = matches?.firstOrNull()

            val command = getHomeCommand(context = context, recognizedText = recognizedText)
            viewModel.onEvent(
                event = HomeEvent.ProcessRecognizedText(
                    recognizedText = recognizedText,
                    command = command,
                ),
            )
        }
    }

    fun startSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, java.util.Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, speechInputDialogMessage)
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
                            message = unexpectedErrorMessage,
                            withDismissAction = true,
                        )
                    }
                }
                is HomeSideEffect.NavigateTo -> {
                    when (effect.command) {
                        HomeCommand.NEW_EXPENSE -> navigateToNewExpense()
                        HomeCommand.EXPENSES -> navigateToExpenses()
                        HomeCommand.ANALYTICS -> navigateToAnalytics()
                        else -> {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = unknownCommandMessage,
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

private fun getHomeCommand(context: Context, recognizedText: String?): HomeCommand {
    if (recognizedText?.contains(
            other = context.getString(R.string.new_expense_command),
            ignoreCase = true,
        ) == true
    ) {
        return HomeCommand.NEW_EXPENSE
    }
    if (recognizedText?.contains(
            other = context.getString(R.string.expenses_command),
            ignoreCase = true,
        ) == true
    ) {
        return HomeCommand.EXPENSES
    }
    if (recognizedText?.contains(
            other = context.getString(R.string.analytics_command),
            ignoreCase = true,
        ) == true
    ) {
        return HomeCommand.ANALYTICS
    }
    if (recognizedText?.contains(
            other = context.getString(R.string.show_commands),
            ignoreCase = true,
        ) == true
    ) {
        return HomeCommand.COMMANDS
    }

    return HomeCommand.ERROR
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
            ListItem(
                headlineContent = {
                    val profit = if (state.isProfitVisible) {
                        state.profit.toString()
                    } else {
                        stringResource(id = R.string.dots)
                    }

                    Text(text = profit)
                },
                trailingContent = {
                    IconButton(onClick = { onEvent(HomeEvent.ToggleProfitVisibility) }) {
                        if (state.isProfitVisible) {
                            Icon(
                                imageVector = Icons.Outlined.Visibility,
                                contentDescription = stringResource(
                                    id = R.string.profit_visibility_on,
                                ),
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Outlined.VisibilityOff,
                                contentDescription = stringResource(
                                    id = R.string.profit_visibility_off,
                                ),
                            )
                        }
                    }
                },
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp, end = 4.dp, bottom = 4.dp)
                        .clip(shape = RoundedCornerShape(5))
                        .background(color = MaterialTheme.colorScheme.primary)
                        .sizeIn(minHeight = 200.dp)
                        .weight(1f)
                        .clickable { onEvent(HomeEvent.NavigateToNewExpense) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = stringResource(id = R.string.new_expense))
                }

                Box(
                    modifier = Modifier
                        .padding(start = 4.dp, top = 8.dp, end = 8.dp, bottom = 4.dp)
                        .clip(shape = RoundedCornerShape(5))
                        .background(color = MaterialTheme.colorScheme.primary)
                        .sizeIn(minHeight = 200.dp)
                        .weight(1f)
                        .clickable { onEvent(HomeEvent.NavigateToExpenses) }
                        .background(color = MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(id = R.string.expenses),
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 4.dp, end = 4.dp, bottom = 8.dp)
                        .clip(shape = RoundedCornerShape(5))
                        .background(color = MaterialTheme.colorScheme.primary)
                        .sizeIn(minHeight = 200.dp)
                        .weight(1f)
                        .clickable { onEvent(HomeEvent.NavigateToAnalytics) }
                        .background(color = MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = stringResource(id = R.string.analytics))
                }

                Box(
                    modifier = Modifier
                        .padding(start = 4.dp, top = 4.dp, end = 8.dp, bottom = 8.dp)
                        .clip(shape = RoundedCornerShape(5))
                        .background(color = MaterialTheme.colorScheme.primary)
                        .sizeIn(minHeight = 200.dp)
                        .weight(1f)
                        .clickable { onEvent(HomeEvent.ToggleCommandsVisibility) }
                        .background(color = MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(id = R.string.commands),
                    )
                }
            }
        }

        if (state.isCommandsInfoVisible) {
            Card(
                modifier = Modifier
                    .padding(it)
                    .padding(vertical = 88.dp, horizontal = 16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                onClick = { onEvent(HomeEvent.ToggleCommandsVisibility) },
            ) {
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "All Commands",
                    style = MaterialTheme.typography.titleLarge,
                )

                val commandsInfo = listOf(
                    "1. ${stringResource(id = R.string.new_expense_command)} ->" +
                        " ${stringResource(id = R.string.new_expense_command_info)}",
                    "2. ${stringResource(id = R.string.expenses_command)} ->" +
                        " ${stringResource(id = R.string.expenses_command_info)}",
                    "3. ${stringResource(id = R.string.analytics_command)} ->" +
                        " ${stringResource(id = R.string.analytics_command_info)}",
                    "4. ${stringResource(id = R.string.show_commands)} ->" +
                        " ${stringResource(id = R.string.commands_info)}",
                )

                commandsInfo.forEach { info ->
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        text = info,
                    )
                }
            }
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeVisibilityOffPreview() {
    ExpensisTheme {
        HomeContent(
            snackbarHostState = SnackbarHostState(),
            state = HomeState(
                profit = 1000.0,
                isProfitVisible = false,
                isCommandsInfoVisible = true,
            ),
            onEvent = {},
        )
    }
}
