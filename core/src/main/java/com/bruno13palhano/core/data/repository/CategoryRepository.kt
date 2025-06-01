package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun insert(category: Category): Long
    suspend fun update(category: Category): Int
    suspend fun delete(id: Long): Int
    suspend fun getById(id: Long): Category?
    fun getAll(): Flow<List<Category>>
}
