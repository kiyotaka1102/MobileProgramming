package com.example.todolistapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.util.Log

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val onTaskCheckedListener: ((Task, Boolean) -> Unit)? = null,
    private val onEditClickListener: ((Task) -> Unit)? = null,
    private val onDeleteClickListener: ((Task) -> Unit)? = null
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Reference all views from the item_task layout
        val taskTitle = itemView.findViewById<TextView>(R.id.textViewTaskTitle)
        val taskDate = itemView.findViewById<TextView>(R.id.textViewDate)
        val taskTime = itemView.findViewById<TextView>(R.id.textViewTime)
        val taskCategory = itemView.findViewById<TextView>(R.id.textViewCategory)
        val taskPriority = itemView.findViewById<TextView>(R.id.textViewPriority)
        val taskNote = itemView.findViewById<TextView>(R.id.textViewNote)
        val checkBoxDone = itemView.findViewById<CheckBox>(R.id.checkBoxDone)
        val buttonEdit = itemView.findViewById<Button>(R.id.buttonEdit)
        val buttonDelete = itemView.findViewById<Button>(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        Log.d("TaskAdapter", "Binding task at position $position: $task")

        holder.taskTitle.text = task.name
        val dateParts = task.dueDate.split(" ")
        holder.taskDate.text = "Date: ${dateParts[0]}"
        holder.taskTime.text = "Time: ${dateParts.getOrNull(1) ?: ""}"
        holder.taskCategory.text = "Category: ${task.category}"
        holder.taskPriority.text = "Priority: ${task.priority}"
        holder.taskNote.text = "Note: ${task.note}"
        holder.checkBoxDone.isChecked = task.isCompleted

        holder.checkBoxDone.setOnCheckedChangeListener { _, isChecked ->
            onTaskCheckedListener?.invoke(task, isChecked)
        }
        holder.buttonEdit.setOnClickListener {
            onEditClickListener?.invoke(task)
        }
        holder.buttonDelete.setOnClickListener {
            onDeleteClickListener?.invoke(task)
        }
    }

    override fun getItemCount(): Int {
        Log.d("TaskAdapter", "Item count: ${tasks.size}")
        return tasks.size
    }
}