package com.bruno13palhano.expensis.ui.components

import android.content.res.Configuration
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    showDatePicker: Boolean,
    buttonLabel: String,
    dateInMillis: Long,
    onDateChange: (dateInMillis: Long) -> Unit,
    onShowDatePickerChange: (show: Boolean) -> Unit,
) {
    val configuration = LocalConfiguration.current
    var datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { onShowDatePickerChange(false) },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let { dateInMillis ->
                        onDateChange(dateInMillis)
                    }
                }) {
                    Text(text = buttonLabel)
                }
            },
        ) {
            datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = dateInMillis,
                initialDisplayMode =
                if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    DisplayMode.Picker
                } else {
                    DisplayMode.Input
                },
            )
            DatePicker(
                state = datePickerState,
                showModeToggle = configuration.orientation == Configuration.ORIENTATION_PORTRAIT,
            )
        }
    }
}
