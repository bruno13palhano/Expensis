package com.bruno13palhano.expensis.ui.screens.expenses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.expensis.R
import com.bruno13palhano.expensis.ui.components.CustomClickField
import com.bruno13palhano.expensis.ui.components.CustomDatePicker
import com.bruno13palhano.expensis.ui.components.CustomDoubleField
import com.bruno13palhano.expensis.ui.components.CustomTextField
import com.bruno13palhano.expensis.ui.shared.clickableWithoutRipple
import com.bruno13palhano.expensis.ui.shared.currantDateFormatted
import com.bruno13palhano.expensis.ui.shared.currentDate
import com.bruno13palhano.expensis.ui.shared.dateFormat
import com.bruno13palhano.expensis.ui.shared.getErrorMessage
import com.bruno13palhano.expensis.ui.shared.rememberFlowWithLifecycle
import com.bruno13palhano.expensis.ui.theme.ExpensisTheme
import kotlinx.coroutines.launch

@Composable
fun ExpenseScreen(
    id: Long,
    navigateBack: () -> Unit,
    viewModel: ExpenseViewModel = hiltViewModel(),
) {
    val state by viewModel.container.state.collectAsStateWithLifecycle()
    val sideEffect = rememberFlowWithLifecycle(flow = viewModel.container.sideEffect)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val errorMessages = getErrorMessage()

    LaunchedEffect(Unit) {
        if (id != 0L) {
            viewModel.onEvent(event = ExpenseEvent.GetExpense(id = id))
            viewModel.onEvent(
                event = ExpenseEvent.UpdateDate(
                    dateInMillis = state.dateInMillis,
                    date = dateFormat.format(state.dateInMillis),
                ),
            )
        } else {
            viewModel.onEvent(
                event = ExpenseEvent.UpdateDate(
                    dateInMillis = currentDate(),
                    date = currantDateFormatted(),
                ),
            )
        }
    }

    LaunchedEffect(Unit) {
        sideEffect.collect { effect ->
            when (effect) {
                is ExpenseSideEffect.ShowError -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = errorMessages[effect.errorType] ?: "",
                            withDismissAction = true,
                        )
                    }
                }
                ExpenseSideEffect.DismissKeyboard -> {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
                ExpenseSideEffect.NavigateBack -> navigateBack()
            }
        }
    }

    ExpenseContent(
        snackbarHostState = snackbarHostState,
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpenseContent(
    snackbarHostState: SnackbarHostState,
    state: ExpenseState,
    onEvent: (event: ExpenseEvent) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .consumeWindowInsets(WindowInsets.safeDrawing)
            .clickableWithoutRipple { onEvent(ExpenseEvent.DismissKeyboard) },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.expense)) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(ExpenseEvent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.navigate_back),
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(ExpenseEvent.SaveExpense) }) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(id = R.string.save_expense),
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) {
        Column(modifier = Modifier.padding(it)) {
            CustomTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                value = state.description,
                onValueChange = { description ->
                    onEvent(ExpenseEvent.UpdateDescription(description = description))
                },
                label = stringResource(id = R.string.description),
                placeholder = stringResource(id = R.string.description_placeholder),
            )

            CustomDoubleField(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                value = state.amount,
                onValueChange = { amount -> onEvent(ExpenseEvent.UpdateAmount(amount = amount)) },
                label = stringResource(id = R.string.amount),
                placeholder = stringResource(id = R.string.amount_placeholder),
            )

            CustomClickField(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                value = state.date,
                onClick = { onEvent(ExpenseEvent.ToggleDatePickerVisibility) },
                label = stringResource(id = R.string.date),
                placeholder = "",
            )

            CustomDatePicker(
                showDatePicker = state.isDatePickerVisible,
                buttonLabel = stringResource(id = R.string.date),
                dateInMillis = state.dateInMillis,
                onDateChange = { date ->
                    onEvent(
                        ExpenseEvent.UpdateDate(
                            dateInMillis = date,
                            date = dateFormat.format(date),
                        ),
                    )
                },
                onShowDatePickerChange = { onEvent(ExpenseEvent.ToggleDatePickerVisibility) },
            )

            CustomTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                value = state.activity ?: "",
                onValueChange = { activity ->
                    onEvent(ExpenseEvent.UpdateActivity(activity = activity))
                },
                label = stringResource(id = R.string.activity),
                placeholder = stringResource(id = R.string.activity_placeholder),
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                var icon = Icons.Filled.ToggleOff
                var label = stringResource(id = R.string.debit)

                if (state.isIncome) {
                    icon = Icons.Filled.ToggleOn
                    label = stringResource(id = R.string.income)
                }

                Text(text = label)

                IconToggleButton(
                    checked = state.isIncome,
                    onCheckedChange = { onEvent(ExpenseEvent.ToggleIsIncome) },
                ) {
                    Icon(
                        modifier = Modifier.size(64.dp),
                        imageVector = icon,
                        contentDescription = stringResource(id = R.string.toggle_income),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ExpensePreview() {
    ExpensisTheme {
        ExpenseContent(
            snackbarHostState = SnackbarHostState(),
            state = ExpenseState(),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ExpenseOpenDatePickerPreview() {
    ExpensisTheme {
        ExpenseContent(
            snackbarHostState = SnackbarHostState(),
            state = ExpenseState(isDatePickerVisible = true, isIncome = true),
            onEvent = {},
        )
    }
}
