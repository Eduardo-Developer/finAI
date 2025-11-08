package com.edudev.finai.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(startVersion = 1, endVersion = 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE transactions_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                userId TEXT NOT NULL,
                amount REAL NOT NULL,
                category TEXT NOT NULL,
                description TEXT NOT NULL,
                type TEXT NOT NULL,
                date INTEGER NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            INSERT INTO transactions_new (userId, amount, category, description, type, date)
            SELECT
                '', amount, category, description, type, date
            FROM transactions
        """.trimIndent())

        // 3. Crie o índice ÚNICO na nova tabela.
        db.execSQL("CREATE INDEX `index_transactions_userId` ON `transactions_new`(`userId`)")

        // 4. Apague a tabela antiga.
        db.execSQL("DROP TABLE transactions")

        // 5. Renomeie a nova tabela para o nome original.
        db.execSQL("ALTER TABLE transactions_new RENAME TO transactions")
    }
}
