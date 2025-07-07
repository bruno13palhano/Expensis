package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.dao.ExpenseDao
import com.bruno13palhano.core.data.model.ExpenseEntity
import com.bruno13palhano.core.model.Expense
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao,
) : ExpenseRepository {
    override suspend fun insert(expense: Expense): Long {
        return expenseDao.insert(
            expense = ExpenseEntity(
                id = expense.id,
                description = expense.description,
                amount = expense.amount,
                isIncome = expense.isIncome,
                date = expense.date,
                activity = expense.activity,
            ),
        )
    }

    override suspend fun update(expense: Expense): Int {
        return expenseDao.update(
            expense = ExpenseEntity(
                id = expense.id,
                description = expense.description,
                amount = expense.amount,
                isIncome = expense.isIncome,
                date = expense.date,
                activity = expense.activity,
            ),
        )
    }

    override suspend fun delete(id: Long): Int {
        return expenseDao.delete(id = id)
    }

    override suspend fun getById(id: Long): Expense? {
        val expenseEntity = expenseDao.getById(id = id)

        val expense = expenseEntity?.let { expense ->
            Expense(
                id = expense.id,
                description = expense.description,
                amount = expense.amount,
                isIncome = expense.isIncome,
                date = expense.date,
                activity = expense.activity,
            )
        }

        return expense
    }

    override fun getAll(): Flow<List<Expense>> {
        val categoryCache = hashMapOf<Long, Category>()

        return expenseDao.getAll().map { expenses ->
            expenses.map { entity ->
                Expense(
                    id = entity.id,
                    description = entity.description,
                    amount = entity.amount,
                    isIncome = entity.isIncome,
                    date = entity.date,
                    activity = entity.activity,
                )
            }
        }
    }
}
