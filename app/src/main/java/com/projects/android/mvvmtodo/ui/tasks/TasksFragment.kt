package com.projects.android.mvvmtodo.ui.tasks

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.projects.android.mvvmtodo.R
import com.projects.android.mvvmtodo.data.Task
import com.projects.android.mvvmtodo.databinding.FragmentTasksBinding
import com.projects.android.mvvmtodo.utils.DataStoreManager
import com.projects.android.mvvmtodo.utils.OnQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TasksFragment : Fragment(R.layout.fragment_tasks), TaskAdapter.OnItemClickListener {
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var preferencesManager: DataStoreManager
    private lateinit var newList: MutableList<Task>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTasksBinding.bind(view)
        taskAdapter = TaskAdapter(this)


        preferencesManager = viewModel.getPreferencesManager()

        binding.apply {
            recyclerViewTasks.apply {
                layoutManager = LinearLayoutManager(this@TasksFragment.requireContext())
                setHasFixedSize(true)
                adapter = taskAdapter
            }
            fabAddTask.setOnClickListener {
                val action = TasksFragmentDirections.actionTaskFragmentToAddEditTaskFragment(title = "Add Task")
                findNavController().navigate(action)
            }
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task = taskAdapter.currentList[viewHolder.adapterPosition]

                    viewModel.deleteTask(task)

                    Snackbar.make(view, "Task Deleted!", Snackbar.LENGTH_LONG).setAction("UNDO") {
                        viewModel.insert(task)
                    }.show()
                }

            }).attachToRecyclerView(recyclerViewTasks)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)

        val searchView = menu.findItem(R.id.action_search).actionView as androidx.appcompat.widget.SearchView
        searchView.OnQueryTextChanged {
            viewModel.getTasks(it.orEmpty()).observe(viewLifecycleOwner, Observer {
                taskAdapter.submitList(it)
            })
        }

        viewLifecycleOwner.lifecycleScope.launch {
            preferencesManager.getHideCompletedState().collect { preferences ->
                if (preferences != null) {
                    menu.findItem(R.id.action_hide_completed).isChecked = preferences
                }

                when (preferences) {
                    true -> {
                        newList = mutableListOf()
                        viewModel.getTasks().observe(viewLifecycleOwner, Observer {
                            for (task in it) {
                                if (!task.completed) {
                                    newList.add(task)
                                }
                            }
                            taskAdapter.submitList(newList)
                        })
                    }
                    false -> {
                        viewModel.getTasks().observe(viewLifecycleOwner, Observer {
                            taskAdapter.submitList(it)
                        })
                    }
                }


            }
        }




    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {

                return true
            }

            R.id.action_sort_by_date -> {
                viewLifecycleOwner.lifecycleScope.launch {
                    preferencesManager.saveSortByDate(true)
                    preferencesManager.saveSortByName(false)
                }
                viewModel.getTasksSortedByDate().observe(viewLifecycleOwner, Observer {
                    taskAdapter.submitList(it)
                })
                return true
            }

            R.id.action_sort_by_name -> {
                viewLifecycleOwner.lifecycleScope.launch {
                    preferencesManager.saveSortByDate(false)
                    preferencesManager.saveSortByName(true)
                }
                viewModel.getTasksSortedByName().observe(viewLifecycleOwner, Observer {
                    taskAdapter.submitList(it)
                })
                return true
            }

            R.id.action_hide_completed -> {
                item.isChecked = !item.isChecked
                viewLifecycleOwner.lifecycleScope.launch {
                    preferencesManager.saveHideCompleted(item.isChecked)
                }
                newList = mutableListOf()
                when (!item.isChecked) {
                    true -> viewModel.getTasks().observe(viewLifecycleOwner, Observer {
                        taskAdapter.submitList(it)
                    })
                    false -> viewModel.getTasks().observe(viewLifecycleOwner, Observer {
                        for (task in it) {
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
                val alertDialog = AlertDialog.Builder(requireContext())
                        .setTitle("Are You Sure?")
                        .setMessage("This Will delete all the completed tasks. This action can't be undone.")
                        .setPositiveButton("Delete") {_, _ ->
                            viewModel.deleteCompletedTasks()
                        }
                        .setNegativeButton("cancel") {dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(task: Task) {
        val action = TasksFragmentDirections.actionTaskFragmentToAddEditTaskFragment(task, "Edit Task")
        findNavController().navigate(action)
    }

    override fun onCheckBoxClicked(task: Task, isChecked: Boolean) {
        viewModel.update(task.copy(completed = isChecked))
    }
}