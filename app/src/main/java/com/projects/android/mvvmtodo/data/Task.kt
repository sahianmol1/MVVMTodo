package com.projects.android.mvvmtodo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat

@Entity(tableName = "table_task")
data class Task(
    val name: String,
    val important: Boolean,
    val completed: Boolean,
    val dateCreated: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) {
    val dateCreatedFormatted: String
        get() = DateFormat.getDateTimeInstance().format(dateCreated)
}