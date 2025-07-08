package com.bruno13palhano.expensis.ui.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bruno13palhano.expensis.R

enum class ErrorType {
    INVALID_FIELD,
    UNEXPECTED,
}

@Composable
fun getErrorMessage(): HashMap<ErrorType, String> {
    return hashMapOf(
        ErrorType.INVALID_FIELD to stringResource(id = R.string.invalid_field_error),
        ErrorType.UNEXPECTED to stringResource(id = R.string.unexpected_error),
    )
}
