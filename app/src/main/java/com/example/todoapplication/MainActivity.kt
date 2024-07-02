package com.example.todoapplication

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)
        val tasks = databaseHelper.getAllTasks().toMutableList()

        recyclerView = findViewById(R.id.recyclerView)
        addButton = findViewById(R.id.addButton)

        taskAdapter = TaskAdapter(tasks, ::deleteTask, ::updateTask)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = taskAdapter

        addButton.setOnClickListener {
            showAddTaskDialog()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showAddTaskDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null)
        val taskEditText = dialogView.findViewById<EditText>(R.id.taskEditText)

        AlertDialog.Builder(this)
            .setTitle("Add Task")
            .setView(dialogView)
            .setPositiveButton("Save") { dialog, _ ->
                val task = Task(task = taskEditText.text.toString())
                val result = databaseHelper.addTask(task)
                if (result != -1L) {
                    task.id = result.toInt()
                    (taskAdapter.tasks as MutableList).add(task)
                    taskAdapter.notifyDataSetChanged()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun deleteTask(id: Int) {
        databaseHelper.deleteTask(id)
        val index = taskAdapter.tasks.indexOfFirst { it.id == id }
        if (index != -1) {
            (taskAdapter.tasks as MutableList).removeAt(index)
            taskAdapter.notifyItemRemoved(index)
        }
    }

    private fun updateTask(task: Task) {
        databaseHelper.updateTask(task)
        taskAdapter.notifyDataSetChanged() // Notify adapter of changes
    }
}