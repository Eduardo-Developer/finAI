package com.edudev.finai.di

import android.content.Context
import androidx.room.Room
import com.edudev.finai.data.local.dao.TransactionDao
import com.edudev.finai.data.local.database.FinAIDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FinAIDatabase {
        return Room.databaseBuilder(
            context,
            FinAIDatabase::class.java,
            "finai_database"
        ).build()
    }

    @Provides
    fun provideTransactionDao(database: FinAIDatabase): TransactionDao {
        return database.transactionDao()
    }
}
