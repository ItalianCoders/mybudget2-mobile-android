/*
 * Project: mybudget2-mobile-android
 * File: AppDatabase.kt
 *
 * Created by fattazzo
 * Copyright © 2019 Gianluca Fattarsi. All rights reserved.
 *
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package it.italiancoders.mybudget.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import it.italiancoders.mybudget.db.converter.BigDecimalConverter
import it.italiancoders.mybudget.db.dao.CategoryDao
import it.italiancoders.mybudget.db.dao.ExpenseSummaryDao
import it.italiancoders.mybudget.db.dao.MovementDao
import it.italiancoders.mybudget.db.entity.Category
import it.italiancoders.mybudget.db.entity.ExpenseSummary
import it.italiancoders.mybudget.db.entity.Movement

@Database(
    entities = [Category::class, Movement::class, ExpenseSummary::class],
    version = 5,
    exportSchema = true
)
@TypeConverters(BigDecimalConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun movementDao(): MovementDao
    abstract fun expenseSummaryDao(): ExpenseSummaryDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "myBudget.db"
        ).fallbackToDestructiveMigration()
            .build()

        /**
         * Delete e rebuld the database
         */
        fun clearAllData(context: Context) {
            context.deleteDatabase(instance?.openHelper?.databaseName)
            instance = null
            invoke(context)
        }

        /**
         * Get the database file size in MB
         */
        fun getSize(context: Context): Float? =
            try {
                val dbFile = context.getDatabasePath(instance?.openHelper?.databaseName)
                val dbFileWal = context.getDatabasePath("${instance?.openHelper?.databaseName}-wal")
                val dbFileShm = context.getDatabasePath("${instance?.openHelper?.databaseName}-shm")
                (dbFile.length() + dbFileWal.length() + dbFileShm.length()) / (1024f * 1024f)
            } catch (e: Exception) {
                null
            }
    }

}