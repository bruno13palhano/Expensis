package com.bruno13palhano.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bruno13palhano.core.data.dao.ExpenseDao
import com.bruno13palhano.core.data.model.ExpenseEntity

@Database(
    entities = [
        ExpenseEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
}
