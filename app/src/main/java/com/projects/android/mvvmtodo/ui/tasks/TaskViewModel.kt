package com.projects.android.mvvmtodo.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.projects.android.mvvmtodo.data.TaskDao

class TaskViewModel @ViewModelInject constructor(private val taskDao: TaskDao): ViewModel() {

    fun getAllTasks() = taskDao.getAllTasks().asLiveData()
}