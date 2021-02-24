package com.projects.android.mvvmtodo.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query ("SELECT * from table_task WHERE name LIKE '%' || :searchQuery || '%'  order by important desc, name")
    fun getTasksSortedByName(searchQuery: String): Flow<List<Task>>

    @Query("SELECT * from table_task WHERE name LIKE '%' || :searchQuery || '%'  order by important desc, dateCreated desc")
    fun getTasksSortedByDate(searchQuery: String): Flow<List<Task>>

    @Query("DELETE FROM table_task WHERE completed=1")
    suspend fun deleteCompletedTasks()
}