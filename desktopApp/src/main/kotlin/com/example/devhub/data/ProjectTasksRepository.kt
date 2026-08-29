package com.example.devhub.data

import java.io.File

object ProjectTasksRepository {

    // Сохранить список задач в файл проекта
    fun saveTasks(projectPath: String, tasks: List<KanbanTask>) {
        try {
            val file = File(projectPath, "devhub_tasks.txt")
            // Превращаем задачи в строки формата: ID|||СТАТУС|||ТЕКСТ
            val content = tasks.joinToString("\n") { "${it.id}|||${it.status.name}|||${it.text}" }
            file.writeText(content)
        } catch (e: Exception) {
            println("Ошибка сохранения задач: ${e.message}")
        }
    }

    // Загрузить список задач из файла проекта
    fun loadTasks(projectPath: String): List<KanbanTask> {
        val file = File(projectPath, "devhub_tasks.txt")
        if (!file.exists()) return emptyList()

        val tasks = mutableListOf<KanbanTask>()
        try {
            file.readLines().forEach { line ->
                if (line.isNotBlank()) {
                    val parts = line.split("|||")
                    if (parts.size == 3) {
                        tasks.add(
                            KanbanTask(
                                id = parts[0].toLong(),
                                status = TaskStatus.valueOf(parts[1]),
                                text = parts[2]
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            println("Ошибка загрузки задач: ${e.message}")
        }
        return tasks
    }
}