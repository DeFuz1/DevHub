package com.example.devhub.ui.components.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.example.devhub.data.KanbanTask
import com.example.devhub.data.LocalProject
import com.example.devhub.data.ProjectTasksRepository
import com.example.devhub.data.TaskStatus

@Composable
fun KanbanScreen(selectedProject: LocalProject?) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (selectedProject != null) {
            // Умный список задач
            val kanbanTasks = remember(selectedProject) {
                mutableStateListOf<KanbanTask>().apply {
                    addAll(ProjectTasksRepository.loadTasks(selectedProject.path))
                }
            }
            var newTaskText by remember { mutableStateOf("") }

            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                // Верхняя панель добавления задачи
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextField(
                        value = newTaskText,
                        onValueChange = { newTaskText = it },
                        placeholder = { Text("Новая задача...", color = Color.White) },
                        modifier = Modifier.weight(1f),
                        // Делаем вводимый текст белым и убираем серый фон подложки
                        colors = androidx.compose.material.TextFieldDefaults.textFieldColors(
                            textColor = Color.White,
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                    Button(onClick = {
                        if (newTaskText.isNotBlank()) {
                            val newTask = KanbanTask(
                                text = newTaskText,
                                status = TaskStatus.TODO
                            )
                            kanbanTasks.add(newTask)
                            ProjectTasksRepository.saveTasks(selectedProject.path, kanbanTasks)
                            newTaskText = ""
                        }
                    }) {
                        Text("Добавить")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Три колонки Канбана
                Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    TaskStatus.values().forEach { currentStatus ->
                        val tasksInColumn = kanbanTasks.filter { it.status == currentStatus }

                        val columnTitle = when(currentStatus) {
                            TaskStatus.TODO -> "📥 Надо сделать"
                            TaskStatus.IN_PROGRESS -> "⚡ В процессе"
                            TaskStatus.DONE -> "✅ Готово"
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(Color.DarkGray)
                                .padding(8.dp)
                        ) {
                            Text(columnTitle, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))

                            tasksInColumn.forEach { task ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .background(Color.Gray)
                                        .clickable {
                                            val index = kanbanTasks.indexOfFirst { it.id == task.id }
                                            if (index != -1) {
                                                val nextStatus = when (task.status) {
                                                    TaskStatus.TODO -> TaskStatus.IN_PROGRESS
                                                    TaskStatus.IN_PROGRESS -> TaskStatus.DONE
                                                    TaskStatus.DONE -> TaskStatus.TODO
                                                }
                                                kanbanTasks[index] = kanbanTasks[index].copy(status = nextStatus)
                                                ProjectTasksRepository.saveTasks(selectedProject.path, kanbanTasks)
                                            }
                                        }
                                        .padding(8.dp)
                                ) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(task.text, modifier = Modifier.weight(1f), color = Color.White)
                                        Text(
                                            text = "❌",
                                            modifier = Modifier.clickable {
                                                kanbanTasks.removeIf { it.id == task.id }
                                                ProjectTasksRepository.saveTasks(selectedProject.path, kanbanTasks)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Выберите проект на первой вкладке, чтобы увидеть Канбан-доску", color = Color.Gray)
            }
        }
    }
}
