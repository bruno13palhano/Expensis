package com.bruno13palhano.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.core.data.model.CategoryEntity

@Dao
internal interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoryEntity): Long

    @Update
    suspend fun update(category: CategoryEntity): Int

    @Query("DELETE FROM Category WHERE id = :id")
    suspend fun delete(id: Long): Int

    @Query("SELECT * FROM Category WHERE id = :id")
    suspend fun getById(id: Long): CategoryEntity?

    @Query("SELECT * FROM Category")
    suspend fun getAll(): CategoryEntity
}
