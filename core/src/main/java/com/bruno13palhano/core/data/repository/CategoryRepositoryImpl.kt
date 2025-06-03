package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.dao.CategoryDao
import com.bruno13palhano.core.data.model.CategoryEntity
import com.bruno13palhano.core.model.Category
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
) : CategoryRepository {
    override suspend fun insert(category: Category): Long {
        return categoryDao.insert(
            category = CategoryEntity(id = category.id, name = category.name),
        )
    }

    override suspend fun update(category: Category): Int {
        return categoryDao.update(
            category = CategoryEntity(id = category.id, name = category.name),
        )
    }

    override suspend fun delete(id: Long): Int {
        return categoryDao.delete(id = id)
    }

    override suspend fun getById(id: Long): Category? {
        val categoryEntity = categoryDao.getById(id = id)

        val category: Category? = categoryEntity?.let {
            Category(id = it.id, name = it.name)
        }

        return category
    }

    override fun getAll(): Flow<List<Category>> {
        return categoryDao.getAll().map {
            it.map { category ->
                Category(id = category.id, name = category.name)
            }
        }
    }
}
