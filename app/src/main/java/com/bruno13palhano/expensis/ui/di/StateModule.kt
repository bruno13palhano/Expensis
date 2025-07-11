package com.bruno13palhano.expensis.ui.di

import com.bruno13palhano.expensis.ui.screens.expenses.ExpenseState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StateModule {
    @Provides
    @Singleton
    fun provideExpenseState() = ExpenseState()
}
