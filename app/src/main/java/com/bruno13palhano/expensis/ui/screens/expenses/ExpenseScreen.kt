package com.bruno13palhano.expensis.ui.screens.expenses

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bruno13palhano.expensis.ui.components.CustomClickField
import com.bruno13palhano.expensis.ui.components.CustomDatePicker
import com.bruno13palhano.expensis.ui.components.CustomDoubleField
import com.bruno13palhano.expensis.ui.components.CustomTextField
import com.bruno13palhano.expensis.ui.theme.ExpensisTheme

@Composable
fun ExpenseScreen(navigateBack: () -> Unit, viewModel: ExpenseViewModel = viewModel()) {
    ExpenseContent(
        label = viewModel.label,
        amount = viewModel.amount,
        category = viewModel.category,
        date = viewModel.date,
        dateInMillis = 0L,
        datePickerVisibility = viewModel.datePickerVisibility,
        onLabelChange = viewModel::updateLabel,
        onAmountChange = viewModel::updateAmount,
        onCategoryChange = viewModel::updateCategory,
        onDateInMillisChange = {},
        onShowDatePickerChange = {},
        navigateBack = navigateBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpenseContent(
    label: String,
    amount: String,
    category: String,
    date: String,
    dateInMillis: Long,
    datePickerVisibility: Boolean,
    onLabelChange: (label: String) -> Unit,
    onAmountChange: (amount: String) -> Unit,
    onCategoryChange: (category: String) -> Unit,
    onDateInMillisChange: (dateInMillis: Long) -> Unit,
    onShowDatePickerChange: (showDatePicker: Boolean) -> Unit,
    navigateBack: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.consumeWindowInsets(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = { Text("Expense") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = null,
                )
            }
        },
    ) {
        Column(modifier = Modifier.padding(it)) {
            CustomTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                value = label,
                onValueChange = onLabelChange,
                label = "Label",
                placeholder = "Enter label",
            )
            CustomDoubleField(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                value = amount,
                onValueChange = onAmountChange,
                label = "Amount",
                placeholder = "Enter amount",
            )
            CustomClickField(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                value = category,
                onClick = {
                },
                label = "Category",
                placeholder = "Enter category",
            )
            CustomTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                value = date,
                onValueChange = {},
                label = "Date",
                placeholder = "Enter date",
            )

            CustomDatePicker(
                showDatePicker = datePickerVisibility,
                buttonLabel = "Date",
                dateInMillis = dateInMillis,
                onDateChange = onDateInMillisChange,
                onShowDatePickerChange = onShowDatePickerChange,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ExpensePreview() {
    ExpensisTheme {
        ExpenseContent(
            label = "",
            amount = "",
            category = "",
            date = "",
            dateInMillis = 0L,
            datePickerVisibility = false,
            onLabelChange = {},
            onAmountChange = {},
            onCategoryChange = {},
            onDateInMillisChange = {},
            onShowDatePickerChange = {},
            navigateBack = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ExpenseOpenDatePickerPreview() {
    ExpensisTheme {
        ExpenseContent(
            label = "",
            amount = "",
            category = "",
            date = "",
            dateInMillis = 0L,
            datePickerVisibility = true,
            onLabelChange = {},
            onAmountChange = {},
            onCategoryChange = {},
            onDateInMillisChange = {},
            onShowDatePickerChange = {},
            navigateBack = {},
        )
    }
}
