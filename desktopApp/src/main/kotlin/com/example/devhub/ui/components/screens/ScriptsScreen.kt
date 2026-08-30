package com.example.devhub.ui.components.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devhub.data.CommandRunner
import com.example.devhub.data.LocalProject
import com.example.devhub.data.ProjectConfigRepository

// Простая структура для отображения кнопок на экране
data class IdeButtonInfo(
    val name: String,
    val iconEmoji: String,
    val savedPath: String? // Путь, который пришел из репозитория настроек
)

@Composable
fun ScriptsScreen(selectedProject: LocalProject?) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF1E1E1E))) {
        if (selectedProject != null) {

            // Вытаскиваем кастомные пути из репозитория настроек
            val studioPath = remember { ProjectConfigRepository.getCustomStudioPath() }
            val ideaPath = remember { ProjectConfigRepository.getCustomIdeaPath() }
            val vsCodePath = remember { ProjectConfigRepository.getCustomVsCodePath() }

            // Формируем список сред для отрисовки интерфейса
            val registeredIdes = remember(studioPath, ideaPath, vsCodePath) {
                listOf(
                    IdeButtonInfo("Android Studio", "🤖", studioPath),
                    IdeButtonInfo("IntelliJ IDEA", "☕", ideaPath),
                    IdeButtonInfo("VS Code", "💻", vsCodePath)
                )
            }

            // Фильтруем список: показываем кнопку только если пользователь заполнил путь в настройках
            val configuredIdes = remember(registeredIdes) {
                registeredIdes.filter { !it.savedPath.isNullOrBlank() }
            }

            Column(modifier = Modifier.fillMaxSize().padding(32.dp)) {
                Text(text = "⚡ Script Runner", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "Рабочая папка: ${selectedProject.name}",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Кнопка проводника — она системная и выводится всегда через стандартный метод
                    ScriptActionButton(
                        text = "📂 Открыть папку в Проводнике Windows",
                        onClick = { CommandRunner.runConsoleCommand(selectedProject.path, "explorer .") }
                    )

                    // Динамически выводим кнопки для IDE, которые пользователь настроил
                    configuredIdes.forEach { ide ->
                        ScriptActionButton(
                            text = "${ide.iconEmoji} Открыть исходный код в ${ide.name}",
                            onClick = {
                                // Передаем кастомный путь и папку проекта в наш объект-исполнитель
                                CommandRunner.openProjectInIde(ide.savedPath!!, selectedProject.path)
                            }
                        )
                    }

                    // Если пользователь ещё не заполнил настройки путей, показываем стильную подсказку
                    if (configuredIdes.isEmpty()) {
                        Text(
                            text = "💡 Чтобы здесь появились кнопки сред разработки, укажите пути к их .exe файлам на вкладке настроек 🛠️",
                            color = Color(0xFF555555),
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(top = 8.dp).fillMaxWidth(0.6f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScriptActionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFF8A2BE2),
            contentColor = Color.White
        ),
        modifier = Modifier.fillMaxWidth(0.5f).height(48.dp)
    ) {
        Text(text = text, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}
