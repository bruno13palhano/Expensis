package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    suspend fun insert(expense: Expense): Long
    suspend fun update(expense: Expense): Int
    suspend fun delete(id: Long): Int
    suspend fun getById(id: Long): Expense
    fun getAll(): Flow<List<Expense>>
}
