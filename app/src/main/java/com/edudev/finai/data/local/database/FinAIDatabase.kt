package com.edudev.finai.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.edudev.finai.data.local.dao.TransactionDao
import com.edudev.finai.data.local.entity.TransactionEntity
import com.edudev.finai.data.local.entity.Converters

@Database(
    entities = [TransactionEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FinAIDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}
