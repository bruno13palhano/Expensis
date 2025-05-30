package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.dao.ExpenseDao
import com.bruno13palhano.core.model.Expense
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao,
) : ExpenseRepository {
    override suspend fun insert(expense: Expense): Long {
        TODO("Not yet implemented")
    }

    override suspend fun update(expense: Expense): Int {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Long): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: Long): Expense {
        TODO("Not yet implemented")
    }

    override fun getAll(): Flow<List<Expense>> {
        TODO("Not yet implemented")
    }
}
