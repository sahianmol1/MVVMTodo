package com.projects.android.mvvmtodo.ui.tasks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.projects.android.mvvmtodo.R
import com.projects.android.mvvmtodo.databinding.FragmentTasksBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksFragment: Fragment(R.layout.fragment_tasks) {
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var taskAdapter: TaskAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskAdapter = TaskAdapter()

        val binding = FragmentTasksBinding.bind(view)

        binding.apply {
            recyclerViewTasks.apply {
                layoutManager = LinearLayoutManager(this@TasksFragment.requireContext())
                setHasFixedSize(true)
                adapter = taskAdapter
            }
        }
        viewModel.getAllTasks().observe(viewLifecycleOwner, Observer {
            taskAdapter.submitList(it)
        })
    }
}