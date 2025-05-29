package com.bruno13palhano.core.model

data class Expense(
    val id: Long,
    val label: String,
    val amount: Double,
    val date: Long
)
