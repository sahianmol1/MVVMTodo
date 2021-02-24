package com.projects.android.mvvmtodo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun getDao(): TaskDao

    class Callback @Inject constructor(
        val database: Provider<TaskDatabase>
    ): RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().getDao()
            CoroutineScope(SupervisorJob()).launch {
                dao.insert(Task("Call Elon Musk", true, false))
                dao.insert(Task("Call Mom", true, false))
                dao.insert(Task("Watch Movie", false, false))
                dao.insert(Task("Play Cricket", false, false))
                dao.insert(Task("Do Coding", true, true))
            }
        }
    }


}