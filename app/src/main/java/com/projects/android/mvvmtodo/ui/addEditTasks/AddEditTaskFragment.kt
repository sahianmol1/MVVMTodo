package com.projects.android.mvvmtodo.ui.addEditTasks

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.projects.android.mvvmtodo.R
import com.projects.android.mvvmtodo.data.Task
import com.projects.android.mvvmtodo.databinding.FragmentAddEditTaskBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {

    private val args by navArgs<AddEditTaskFragmentArgs>()
    private val viewModel: AddEditTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddEditTaskBinding.bind(view)

        val task = args.task
        binding.apply {

            if (task == null) {
                textViewDateCreated.visibility = View.GONE
            } else {
                editTextTaskName.setText(task.name)
                checkBoxImportant.isChecked = task.important
                textViewDateCreated.visibility = View.VISIBLE
                textViewDateCreated.text = task.dateCreatedFormatted
            }

            fabSaveTask.setOnClickListener {
                if (editTextTaskName.text.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "Name Cannot be empty", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else {
                    if (task == null) {
                        viewModel.insert(Task(name = editTextTaskName.text.toString(),
                        important = checkBoxImportant.isChecked))
                        Toast.makeText(requireContext(), "Task Added", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    } else {
                        viewModel.update(task.copy(name = editTextTaskName.text.toString(),
                        important = checkBoxImportant.isChecked))
                        Toast.makeText(requireContext(), "Task Updated", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                }
            }
        }


    }
}