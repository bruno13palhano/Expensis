package com.bruno13palhano.expensis.ui.components

import android.icu.text.DecimalFormat
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.bruno13palhano.expensis.ui.shared.clearFocusOnKeyboardDismiss
import java.util.Locale

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    icon: @Composable (() -> Unit)? = null,
    label: String,
    placeholder: String,
    isError: Boolean = false,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
) {
    OutlinedTextField(
        modifier = modifier.clearFocusOnKeyboardDismiss(),
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        leadingIcon = icon,
        label = {
            Text(
                text = label,
                fontStyle = FontStyle.Italic,
            )
        },
        placeholder = {
            Text(
                text = placeholder,
                fontStyle = FontStyle.Italic,
            )
        },
        singleLine = singleLine,
        readOnly = readOnly,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { defaultKeyboardAction(ImeAction.Done) }),
    )
}

@Composable
fun CustomDoubleField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (value: String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    label: String,
    placeholder: String,
    isError: Boolean = false,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
) {
    val decimalFormat = DecimalFormat.getInstance(Locale.getDefault()) as DecimalFormat
    val decimalSeparator = decimalFormat.decimalFormatSymbols.decimalSeparator
    val pattern = remember { Regex("^\\d*\\$decimalSeparator?\\d*\$") }

    OutlinedTextField(
        modifier = modifier.clearFocusOnKeyboardDismiss(),
        value = value,
        onValueChange = { newValue ->
            if (newValue.isEmpty() || newValue.matches(pattern)) {
                onValueChange(newValue)
            }
        },
        isError = isError,
        leadingIcon = leadingIcon,
        label = {
            Text(
                text = label,
                fontStyle = FontStyle.Italic,
            )
        },
        placeholder = {
            Text(
                text = placeholder,
                fontStyle = FontStyle.Italic,
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Decimal,
        ),
        keyboardActions = KeyboardActions(onDone = { defaultKeyboardAction(ImeAction.Done) }),
        singleLine = singleLine,
        readOnly = readOnly,
    )
}

@Composable
fun CustomClickField(
    modifier: Modifier = Modifier,
    value: String,
    onClick: () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    label: String,
    placeholder: String,
    singleLine: Boolean = true,
    readOnly: Boolean = true,
) {
    OutlinedTextField(
        modifier = modifier
            .onFocusChanged { focusState ->
                if (focusState.hasFocus) {
                    onClick()
                }
            },
        value = value,
        onValueChange = {},
        leadingIcon = leadingIcon,
        label = {
            Text(
                text = label,
                fontStyle = FontStyle.Italic,
            )
        },
        placeholder = {
            Text(
                text = placeholder,
                fontStyle = FontStyle.Italic,
            )
        },
        singleLine = singleLine,
        readOnly = readOnly,
    )
}
