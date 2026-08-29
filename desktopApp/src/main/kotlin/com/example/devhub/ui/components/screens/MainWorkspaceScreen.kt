package com.example.devhub.ui.components.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.example.devhub.ui.components.NavigationSideBar

import com.example.devhub.data.LocalProject
import com.example.devhub.data.ProjectManager
import com.example.devhub.data.ProjectConfigRepository
import com.example.devhub.data.ProjectAnalyzer


enum class AppScreen {
    PROJECTS,
    NOTES,
    KANBAN,
    SCRIPTS,
    SETTINGS
}

@Composable
fun MainWorkspaceScreen() {
    var currentScreen by remember { mutableStateOf(AppScreen.PROJECTS) }
    var projects by remember { mutableStateOf(ProjectAnalyzer.getProjects()) }
    var selectedProject by remember { mutableStateOf<LocalProject?>(null) }

    Row(modifier = Modifier.fillMaxSize().background(Color(0xFF2B2B2B))) {
        NavigationSideBar(
            currentScreen = currentScreen,
            onScreenSelected = { currentScreen = it }
        )

        if (currentScreen != AppScreen.SETTINGS) {
            Column(
                modifier = Modifier
                    .width(220.dp)
                    .fillMaxHeight()
                    .background(Color(0xFF3C3F41))
                    .padding(12.dp)
            ) {
                Text("Мои папки", color = Color(0xFFBBBBBB), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                projects.forEach { project ->
                    val isSelected = selectedProject?.path == project.path
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "• ${project.name}",
                            color = if (isSelected) Color(0xFF4A90E2) else Color(0xFFDFDFDF),
                            modifier = Modifier.weight(1f).clickable { selectedProject = project }
                        )
                        Text(
                            text = "❌",
                            color = Color(0xFFE53935),
                            modifier = Modifier.clickable {
                                ProjectConfigRepository.removeProjectPath(project.path)
                                projects = ProjectAnalyzer.getProjects()
                                if (selectedProject?.path == project.path) selectedProject = null
                            }.padding(start = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        val selectedPath = ProjectManager.chooseFolderDialog()
                        if (selectedPath != null) {
                            ProjectConfigRepository.addProjectPath(selectedPath)
                            projects = ProjectAnalyzer.getProjects()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("+ Добавить папку")
                }
            }
        }

        Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
            when (currentScreen) {
                AppScreen.PROJECTS -> {
                    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                        if (selectedProject != null) {
                            Text("Проект: ${selectedProject!!.name}", style = MaterialTheme.typography.h5, color = Color.White)
                            Spacer(modifier = Modifier.height(24.dp))

                            Text("📁 Путь:", color = Color.Gray)
                            Text(selectedProject!!.path, color = Color.LightGray, modifier = Modifier.padding(bottom = 16.dp))

                            Text("🛠️ Использованные технологии:", color = Color.Gray)
                            Text(selectedProject!!.technologies, color = Color.LightGray, modifier = Modifier.padding(bottom = 16.dp))

                            Text("Статус Git-репозитория:", color = Color.Gray)
                            Text(
                                text = if (selectedProject!!.technologies != "Не определено") "Git: Инициализирован" else "Без Git",
                                color = if (selectedProject!!.technologies != "Не определено") Color.Green else Color.Gray,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Text("📅 Добавлен в DevHub:", color = Color.Gray)
                            Text(selectedProject!!.dateAdded, color = Color.LightGray)
                        } else {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("Выберите папку слева", color = Color.Gray)
                            }
                        }
                    }
                }


                AppScreen.SCRIPTS -> {
                    ScriptsScreen(selectedProject = selectedProject)
                }
                AppScreen.NOTES -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Тут будут полноэкранные заметки", color = Color.White) }
                }
                AppScreen.KANBAN -> {
                    KanbanScreen(selectedProject = selectedProject)
                }
                AppScreen.SETTINGS -> {
                    SettingsScreen()
                }
            }
        }
    }
}
