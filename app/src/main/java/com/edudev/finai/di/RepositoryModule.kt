package com.edudev.finai.di

import com.edudev.finai.data.repository.AIRepositoryImpl
import com.edudev.finai.data.repository.AuthRepository
import com.edudev.finai.data.repository.AuthRepositoryImpl
import com.edudev.finai.data.repository.PreferencesRepositoryImpl
import com.edudev.finai.data.repository.ThemeRepository
import com.edudev.finai.data.repository.ThemeRepositoryImpl
import com.edudev.finai.data.repository.TransactionRepositoryImpl
import com.edudev.finai.domain.repository.AIRepository
import com.edudev.finai.domain.repository.PreferencesRepository
import com.edudev.finai.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindAIRepository(
        aiRepositoryImpl: AIRepositoryImpl
    ): AIRepository

    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(
        preferencesRepositoryImpl: PreferencesRepositoryImpl
    ): PreferencesRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindThemeRepository(
        themeRepositoryImpl: ThemeRepositoryImpl
    ): ThemeRepository
}
