package com.whitedot.pomodoro_timer.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Label::class], version = 1, exportSchema = false)
abstract class LabelRoomDatabase : RoomDatabase() {

    abstract fun labelDao(): LabelDao

    private class LabelDatabaseCallback(
            private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.labelDao())
                }
            }
        }

        suspend fun populateDatabase(labelDao: LabelDao) {
            labelDao.deleteAll()

            var label = Label("Study")
            labelDao.insert(label)
            label = Label("Work")
            labelDao.insert(label)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LabelRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): LabelRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LabelRoomDatabase::class.java,
                    "label_database"
                ).addCallback(LabelDatabaseCallback(scope)).build()

                INSTANCE = instance
                instance
            }
        }
    }
}