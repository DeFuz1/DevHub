package com.example.devhub.ui.components.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.devhub.data.KanbanTask
import com.example.devhub.data.LocalProject
import com.example.devhub.data.ProjectTasksRepository
import com.example.devhub.data.TaskStatus

@Composable
fun KanbanScreen(selectedProject: LocalProject?) {
    // Вся область экрана теперь имеет тот самый благородный темный цвет из заметок
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF1E1E1E))) {
        if (selectedProject != null) {
            val kanbanTasks = remember(selectedProject) {
                mutableStateListOf<KanbanTask>().apply {
                    addAll(ProjectTasksRepository.loadTasks(selectedProject.path))
                }
            }
            var newTaskText by remember { mutableStateOf("") }

            Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {

                // 1. ВЕРХНЯЯ ПАНЕЛЬ ДОБАВЛЕНИЯ ЗАДАЧИ
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = newTaskText,
                        onValueChange = { newTaskText = it },
                        placeholder = { Text("Новая задача...", color = Color(0xFF666666)) },
                        // Заменяем Modifier.weight на безопасную функцию rowWeight
                        modifier = Modifier
                            .rowWeight(this, 1f)
                            .background(Color(0xFF2D2D2D), shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 4.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 15.sp),
                        colors = androidx.compose.material.TextFieldDefaults.textFieldColors(
                            textColor = Color.White,
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    Button(
                        onClick = {
                            if (newTaskText.isNotBlank()) {
                                val newTask = KanbanTask(text = newTaskText, status = TaskStatus.TODO)
                                kanbanTasks.add(newTask)
                                ProjectTasksRepository.saveTasks(selectedProject.path, kanbanTasks)
                                newTaskText = ""
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF8A2BE2), // Фиолетовый под ваш стиль кнопки
                            contentColor = Color.White
                        ),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text("Добавить", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 2. ТРИ КОЛОНКИ КАНБАНА
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TaskStatus.entries.forEach { currentStatus ->
                        val tasksInColumn = kanbanTasks.filter { it.status == currentStatus }

                        val columnTitle = when(currentStatus) {
                            TaskStatus.TODO -> "📥 Надо сделать"
                            TaskStatus.IN_PROGRESS -> "⚡ В процессе"
                            TaskStatus.DONE -> "✅ Готово"
                        }

                        // Сама колонка
                        Column(
                            modifier = Modifier
                                .rowWeight(this, 1f)
                                .fillMaxHeight()
                                .background(Color(0xFF252525), shape = RoundedCornerShape(12.dp)) // Мягкие углы, глубокий цвет
                                .padding(12.dp)
                        ) {
                            Text(
                                text = columnTitle,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFBBBBBB),
                                fontSize = 14.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Карточки внутри колонки
                            tasksInColumn.forEach { task ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 5.dp)
                                        .background(Color(0xFF333333), shape = RoundedCornerShape(8.dp)) // Приятный контрастный серый
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
                                        .padding(horizontal = 12.dp, vertical = 10.dp) // Внутренние отступы, чтобы текст "дышал"
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = task.text,
                                            modifier = Modifier.rowWeight(this, 1f),
                                            color = Color(0xFFE0E0E0), // Мягкий белый
                                            fontSize = 14.sp
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        // Изящный текстовый крестик вместо огромного эмодзи
                                        Text(
                                            text = "✕",
                                            color = Color(0xFFE53935),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            modifier = Modifier
                                                .clickable {
                                                    kanbanTasks.removeIf { it.id == task.id }
                                                    ProjectTasksRepository.saveTasks(selectedProject.path, kanbanTasks)
                                                }
                                                .padding(4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Заглушка теперь не сработает, так как обрабатка ее на уровень выше,
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Выберите проект на первой вкладке, чтобы увидеть Канбан-доску", color = Color(0xFF555555))
            }
        }
    }
}
