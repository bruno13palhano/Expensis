package com.bruno13palhano.core.model

data class Expense(
    val id: Long,
    val description: String,
    val amount: Double,
    val isIncome: Boolean,
    val date: Long,
    val activity: String?,
)
