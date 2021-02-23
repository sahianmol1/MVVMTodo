package com.projects.android.mvvmtodo.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.projects.android.mvvmtodo.data.Task
import com.projects.android.mvvmtodo.data.TaskDao
import kotlinx.coroutines.launch

class TaskViewModel @ViewModelInject constructor(private val taskDao: TaskDao): ViewModel() {

    fun getAllTasks(searchQuery: String = "") = taskDao.getAllTasks(searchQuery).asLiveData()

    fun getTasksSortedByName() = taskDao.getTasksSortedByName().asLiveData()

    fun getTasksSortedByDate() = taskDao.getTasksSortedByDate().asLiveData()

    fun deleteCompletedTasks() = viewModelScope.launch {
        taskDao.deleteCompletedTasks()
    }

    fun update(task: Task) = viewModelScope.launch {
        taskDao.update(task)
    }
}