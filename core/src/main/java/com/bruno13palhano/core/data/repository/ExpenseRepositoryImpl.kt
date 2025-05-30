package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.dao.CategoryDao
import com.bruno13palhano.core.data.dao.ExpenseDao
import com.bruno13palhano.core.data.model.ExpenseEntity
import com.bruno13palhano.core.model.Category
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
                label = expense.label,
                amount = expense.amount,
                category = expense.category.id,
                date = expense.date,
            ),
        )
    }

    override suspend fun update(expense: Expense): Int {
        return expenseDao.update(
            expense = ExpenseEntity(
                id = expense.id,
                label = expense.label,
                amount = expense.amount,
                category = expense.category.id,
                date = expense.date,
            ),
        )
    }

    override suspend fun delete(id: Long): Int {
        return expenseDao.delete(id = id)
    }

    override suspend fun getById(id: Long): Expense? {
        val expenseEntity = expenseDao.getById(id = id)

        val expense = expenseEntity?.let { expense ->
            val category = categoryDao.getById(id = expense.category)

            Expense(
                id = expense.id,
                label = expense.label,
                amount = expense.amount,
                category = Category(id = category?.id ?: 0L, name = category?.name ?: ""),
                date = expense.date,
            )
        }

        return expense
    }

    override fun getAll(): Flow<List<Expense>> {
        val categoryCache = hashMapOf<Long, Category>()

        return expenseDao.getAll().map { expenses ->
            expenses.map { entity ->
                var category = categoryCache[entity.category]
                if (category == null) {
                    val categoryEntity = categoryDao.getById(id = entity.category)

                    category = Category(
                        id = categoryEntity?.id ?: 0L,
                        name = categoryEntity?.name ?: "",
                    )

                    categoryCache.put(entity.category, category)
                }

                Expense(
                    id = entity.id,
                    label = entity.label,
                    amount = entity.amount,
                    category = category,
                    date = entity.date,
                )
            }
        }
    }
}
