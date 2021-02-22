package com.projects.android.mvvmtodo.ui.addEditTasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.android.mvvmtodo.data.Task
import com.projects.android.mvvmtodo.data.TaskDao
import kotlinx.coroutines.launch

class AddEditTaskViewModel @ViewModelInject constructor(private val taskDao: TaskDao): ViewModel() {

    fun insert(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
    }

    fun update(task: Task) = viewModelScope.launch {
        taskDao.update(task)
    }
}