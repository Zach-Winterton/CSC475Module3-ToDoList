package com.example.todoapplication

data class Task(
    var id: Int = 0,
    var task: String,
    var isCompleted: Boolean = false
)