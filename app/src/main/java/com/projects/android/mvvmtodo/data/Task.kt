package com.projects.android.mvvmtodo.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity(tableName = "table_task")
@Parcelize
data class Task(
    val name: String,
    val important: Boolean,
    val completed: Boolean = false,
    val dateCreated: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable{
    val dateCreatedFormatted: String
        get() = DateFormat.getDateTimeInstance().format(dateCreated)
}