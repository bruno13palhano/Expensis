package com.bruno13palhano.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.core.data.model.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseEntity): Long

    @Update
    suspend fun update(expense: ExpenseEntity): Int

    @Query("DELETE FROM Expenses WHERE id = :id")
    suspend fun delete(id: Long): Int

    @Query("SELECT * FROM Expenses WHERE id = :id")
    suspend fun getById(id: Long): ExpenseEntity?

    @Query("SELECT * FROM Expenses")
    fun getAll(): Flow<List<ExpenseEntity>>
}
