package com.projects.android.mvvmtodo.ui.tasks

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.projects.android.mvvmtodo.R
import com.projects.android.mvvmtodo.data.Task
import com.projects.android.mvvmtodo.databinding.FragmentTasksBinding
import com.projects.android.mvvmtodo.utils.OnQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksFragment: Fragment(R.layout.fragment_tasks), TaskAdapter.OnItemClickListener {
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var taskAdapter: TaskAdapter
    lateinit var newList: MutableList<Task>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskAdapter = TaskAdapter(this)

        val binding = FragmentTasksBinding.bind(view)

        binding.apply {
            recyclerViewTasks.apply {
                layoutManager = LinearLayoutManager(this@TasksFragment.requireContext())
                setHasFixedSize(true)
                adapter = taskAdapter
            }
            fabAddTask.setOnClickListener {
                val action = TasksFragmentDirections.actionTaskFragmentToAddEditTaskFragment()
                findNavController().navigate(action)
            }
        }
        viewModel.getAllTasks().observe(viewLifecycleOwner, Observer {
            taskAdapter.submitList(it)
        })

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)

        val searchView = menu.findItem(R.id.action_search).actionView as androidx.appcompat.widget.SearchView
        searchView.OnQueryTextChanged {
            viewModel.getAllTasks(it.orEmpty()).observe(viewLifecycleOwner, Observer {
                taskAdapter.submitList(it)
            })
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_search -> {

                return true
            }

            R.id.action_sort_by_date -> {
                viewModel.getTasksSortedByDate().observe(viewLifecycleOwner, Observer {
                    taskAdapter.submitList(it)
                })
                return true
            }

            R.id.action_sort_by_name -> {
                viewModel.getTasksSortedByName().observe(viewLifecycleOwner, Observer {
                    taskAdapter.submitList(it)
                })
                return true
            }

            R.id.action_hide_completed -> {
                item.isChecked = !item.isChecked
                newList = mutableListOf()
                when(!item.isChecked){
                    true -> viewModel.getAllTasks().observe(viewLifecycleOwner, Observer {
                        taskAdapter.submitList(it)
                    })
                    false -> viewModel.getAllTasks().observe(viewLifecycleOwner, Observer {
                        for(task in it) {
                            if (!task.completed) {
                                newList.add(task)
                            }
                        }
                        taskAdapter.submitList(newList)
                    })
                }
                return true
            }

            R.id.action_delete_completed -> {
                viewModel.deleteCompletedTasks()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(task: Task) {
        val action = TasksFragmentDirections.actionTaskFragmentToAddEditTaskFragment(task)
        findNavController().navigate(action)
    }

    override fun onCheckBoxClicked(task: Task, isChecked: Boolean) {
        viewModel.update(task.copy(completed = isChecked))
    }
}