package com.example.devhub.data

// Статус
enum class TaskStatus {
    TODO,        // Надо сделать
    IN_PROGRESS, // В процессе
    DONE         // Готово
}

// Модель одной карточки задачи
data class KanbanTask(
    val id: Long = System.currentTimeMillis() + (0..1000).random(),
    var text: String,
    var status: TaskStatus
)