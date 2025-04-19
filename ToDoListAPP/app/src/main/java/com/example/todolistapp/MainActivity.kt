package com.example.todolistapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import android.widget.PopupMenu

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private val tasks = mutableListOf<Task>()
    private var taskIdCounter = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTasks)
        val fabAddTask = findViewById<FloatingActionButton>(R.id.buttonAddTask)
        val fabSortTasks = findViewById<FloatingActionButton>(R.id.buttonSortTasks)

        // Initialize adapter with click listeners
        taskAdapter = TaskAdapter(
            tasks,
            onTaskCheckedListener = { task, isChecked ->
                task.isCompleted = isChecked
            },
            onEditClickListener = { task ->
                showEditTaskDialog(task)
            },
            onDeleteClickListener = { task ->
                showDeleteConfirmation(task)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = taskAdapter

        fabAddTask.setOnClickListener {
            showAddTaskDialog()
        }

        fabSortTasks.setOnClickListener { view ->
            // Create and show the popup menu
            val popupMenu = PopupMenu(this, view)
            menuInflater.inflate(R.menu.sort_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.sortByName -> {
                        tasks.sortBy { it.name }
                        taskAdapter.notifyDataSetChanged()
                        true
                    }
                    R.id.sortByDate -> {
                        tasks.sortBy { it.dueDate }
                        taskAdapter.notifyDataSetChanged()
                        true
                    }
                    R.id.sortByPriority -> {
                        tasks.sortWith(compareBy {
                            when(it.priority) {
                                "High" -> 0
                                "Medium" -> 1
                                "Low" -> 2
                                else -> 3
                            }
                        })
                        taskAdapter.notifyDataSetChanged()
                        true
                    }
                    R.id.sortByCategory -> {
                        tasks.sortBy { it.category }
                        taskAdapter.notifyDataSetChanged()
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }
    }

    private fun showAddTaskDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_task, null)
        val editTextName = dialogView.findViewById<EditText>(R.id.editTextDialogTaskName)
        val textViewDate = dialogView.findViewById<TextView>(R.id.textViewDialogSelectedDate)
        val buttonPickDate = dialogView.findViewById<Button>(R.id.buttonDialogPickDate)

        // Additional fields for enhanced task
        val spinnerCategory = dialogView.findViewById<Spinner>(R.id.spinnerCategory)
        val spinnerPriority = dialogView.findViewById<Spinner>(R.id.spinnerPriority)
        val editTextNote = dialogView.findViewById<EditText>(R.id.editTextNote)

        // Setup category spinner
        setupCategorySpinner(spinnerCategory)

        // Setup priority spinner
        setupPrioritySpinner(spinnerPriority)

        var selectedDate: String = ""
        var selectedTime: String = ""

        buttonPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()

            // Date picker
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                selectedDate = "$dayOfMonth/${month + 1}/$year"

                // Show time picker after selecting date
                TimePickerDialog(this, { _, hourOfDay, minute ->
                    selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                    textViewDate.text = "Due: $selectedDate $selectedTime"
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Task")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = editTextName.text.toString()
                val category = spinnerCategory.selectedItem.toString()
                val priority = spinnerPriority.selectedItem.toString()
                val note = editTextNote.text.toString()

                if (name.isNotEmpty() && selectedDate.isNotEmpty()) {
                    val newTask = Task(
                        id = taskIdCounter++,
                        name = name,
                        dueDate = "$selectedDate $selectedTime",
                        category = category,
                        priority = priority,
                        note = note
                    )
                    tasks.add(newTask)
                    taskAdapter.notifyItemInserted(tasks.size - 1)
                } else {
                    Toast.makeText(this, "Task name and date are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    private fun showEditTaskDialog(task: Task) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_task, null)
        val editTextName = dialogView.findViewById<EditText>(R.id.editTextDialogTaskName)
        val textViewDate = dialogView.findViewById<TextView>(R.id.textViewDialogSelectedDate)
        val buttonPickDate = dialogView.findViewById<Button>(R.id.buttonDialogPickDate)

        // Additional fields
        val spinnerCategory = dialogView.findViewById<Spinner>(R.id.spinnerCategory)
        val spinnerPriority = dialogView.findViewById<Spinner>(R.id.spinnerPriority)
        val editTextNote = dialogView.findViewById<EditText>(R.id.editTextNote)

        // Setup spinners
        setupCategorySpinner(spinnerCategory)
        setupPrioritySpinner(spinnerPriority)

        editTextName.setText(task.name)
        textViewDate.text = "Due: ${task.dueDate}"
        editTextNote.setText(task.note)

        // Set spinner selection
        val categories = resources.getStringArray(R.array.categories)
        spinnerCategory.setSelection(categories.indexOf(task.category).coerceAtLeast(0))

        val priorities = resources.getStringArray(R.array.priorities)
        spinnerPriority.setSelection(priorities.indexOf(task.priority).coerceAtLeast(0))

        // Current values
        var selectedDate = task.dueDate.split(" ").getOrNull(0) ?: ""
        var selectedTime = task.dueDate.split(" ").getOrNull(1) ?: ""

        buttonPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                selectedDate = "$dayOfMonth/${month + 1}/$year"

                TimePickerDialog(this, { _, hourOfDay, minute ->
                    selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                    textViewDate.text = "Due: $selectedDate $selectedTime"
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle("Edit Task")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val name = editTextName.text.toString()
                val category = spinnerCategory.selectedItem.toString()
                val priority = spinnerPriority.selectedItem.toString()
                val note = editTextNote.text.toString()

                if (name.isNotEmpty() && selectedDate.isNotEmpty()) {
                    val index = tasks.indexOfFirst { it.id == task.id }
                    if (index != -1) {
                        tasks[index] = Task(
                            id = task.id,
                            name = name,
                            dueDate = "$selectedDate $selectedTime",
                            category = category,
                            priority = priority,
                            note = note,
                            isCompleted = task.isCompleted
                        )
                        taskAdapter.notifyItemChanged(index)
                    }
                } else {
                    Toast.makeText(this, "Task name and date are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun showDeleteConfirmation(task: Task) {
        AlertDialog.Builder(this)
            .setTitle("Delete Task")
            .setMessage("Are you sure you want to delete '${task.name}'?")
            .setPositiveButton("Delete") { _, _ ->
                val index = tasks.indexOfFirst { it.id == task.id }
                if (index != -1) {
                    tasks.removeAt(index)
                    taskAdapter.notifyItemRemoved(index)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setupCategorySpinner(spinner: Spinner) {
        val categories = resources.getStringArray(R.array.categories)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setupPrioritySpinner(spinner: Spinner) {
        val priorities = resources.getStringArray(R.array.priorities)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sort_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sortByName -> {
                tasks.sortBy { it.name }
                taskAdapter.notifyDataSetChanged()
                return true
            }
            R.id.sortByDate -> {
                tasks.sortBy { it.dueDate }
                taskAdapter.notifyDataSetChanged()
                return true
            }
            R.id.sortByPriority -> {
                tasks.sortWith(compareBy {
                    when(it.priority) {
                        "High" -> 0
                        "Medium" -> 1
                        "Low" -> 2
                        else -> 3
                    }
                })
                taskAdapter.notifyDataSetChanged()
                return true
            }
            R.id.sortByCategory -> {
                tasks.sortBy { it.category }
                taskAdapter.notifyDataSetChanged()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}