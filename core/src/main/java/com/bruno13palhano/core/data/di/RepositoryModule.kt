package com.bruno13palhano.core.data.di

import com.bruno13palhano.core.data.repository.CategoryRepository
import com.bruno13palhano.core.data.repository.CategoryRepositoryImpl
import com.bruno13palhano.core.data.repository.ExpenseRepository
import com.bruno13palhano.core.data.repository.ExpenseRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindExpenseRepository(repository: ExpenseRepositoryImpl): ExpenseRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(repository: CategoryRepositoryImpl): CategoryRepository
}
