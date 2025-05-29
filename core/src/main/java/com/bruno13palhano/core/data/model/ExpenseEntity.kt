package com.bruno13palhano.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Expense")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val label: String,
    val amount: Double,
    val date: Long,
)
