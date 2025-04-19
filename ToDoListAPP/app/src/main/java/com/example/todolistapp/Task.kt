package com.example.todolistapp

data class Task(
    val id: Int,
    val name: String,
    val dueDate: String,
    val category: String,
    val priority: String,
    val note: String,
    var isCompleted: Boolean = false
) {
    override fun toString(): String {
        return "Task(id=$id, name='$name', dueDate='$dueDate', category='$category', priority='$priority')"
    }
}
