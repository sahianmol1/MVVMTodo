package com.projects.android.mvvmtodo.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.projects.android.mvvmtodo.data.Task
import com.projects.android.mvvmtodo.data.TaskDao
import com.projects.android.mvvmtodo.utils.DataStoreManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TaskViewModel @ViewModelInject constructor(private val taskDao: TaskDao, private val preferencesManager: DataStoreManager): ViewModel() {

    fun getPreferencesManager() = preferencesManager

    fun getTasks(searchQuery: String = ""): LiveData<List<Task>>{

        var sortByDatePreference = false
        var sortByNamePreference = false
        viewModelScope.launch {

            preferencesManager.getSortByDateState().collect { preference ->
                if (preference != null) {
                    sortByDatePreference = preference
                }
            }

            preferencesManager.getSortByNameState().collect { preferences ->
                if (preferences != null) {
                    sortByNamePreference = preferences
                }
            }

        }
        return if (sortByDatePreference) {
            taskDao.getTasksSortedByDate(searchQuery).asLiveData()
        } else if(sortByNamePreference) {
            taskDao.getTasksSortedByName(searchQuery).asLiveData()
        } else {
            taskDao.getTasksSortedByDate(searchQuery).asLiveData()
        }
    }


    fun getTasksSortedByName(searchQuery: String = "") = taskDao.getTasksSortedByName(searchQuery).asLiveData()

    fun getTasksSortedByDate(searchQuery: String = "") = taskDao.getTasksSortedByDate(searchQuery).asLiveData()

    fun deleteCompletedTasks() = viewModelScope.launch {
        taskDao.deleteCompletedTasks()
    }

    fun update(task: Task) = viewModelScope.launch {
        taskDao.update(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        taskDao.delete(task)
    }

    fun insert(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
    }
}