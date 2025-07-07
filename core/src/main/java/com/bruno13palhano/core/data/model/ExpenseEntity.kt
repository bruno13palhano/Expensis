package com.bruno13palhano.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Expenses")
internal data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val description: String,
    val amount: Double,
    val isIncome: Boolean,
    val date: Long,
    val activity: String? = null,
)
