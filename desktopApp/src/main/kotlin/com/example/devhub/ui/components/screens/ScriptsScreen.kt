package com.example.devhub.ui.components.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.devhub.data.CommandRunner
import com.example.devhub.data.LocalProject

@Composable
fun ScriptsScreen(selectedProject: LocalProject?) {
    Column(modifier = Modifier.fillMaxSize().padding(32.dp)) {
        Text("⚡ Script Runner", color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))

        if (selectedProject != null) {
            Text("Рабочая папка: ${selectedProject.name}", color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { CommandRunner.runConsoleCommand(selectedProject.path, "explorer .") }) {
                Text("📂 Открыть папку в Проводнике Windows")
            }
        } else {
            Text("Выберите проект на первой вкладке, чтобы управлять скриптами.", color = Color.Red)
        }
    }
}
