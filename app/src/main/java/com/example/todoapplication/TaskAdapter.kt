package com.example.todoapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    val tasks: MutableList<Task>,
    private val onTaskDeleted: (Int) -> Unit,
    private val onTaskUpdated: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)

        holder.deleteButton.setOnClickListener {
            onTaskDeleted(task.id)
            tasks.removeAt(position)
            notifyItemRemoved(position)
        }

        holder.completedCheckBox.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked
            onTaskUpdated(task)
        }
    }

    override fun getItemCount() = tasks.size

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskTextView: TextView = itemView.findViewById(R.id.taskTextView)
        val completedCheckBox: CheckBox = itemView.findViewById(R.id.completedCheckBox)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        fun bind(task: Task) {
            taskTextView.text = task.task
            completedCheckBox.isChecked = task.isCompleted
        }
    }
}