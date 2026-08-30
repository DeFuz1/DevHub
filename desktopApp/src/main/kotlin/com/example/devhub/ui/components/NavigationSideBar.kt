package com.example.devhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.devhub.ui.components.screens.AppScreen

@Composable
fun NavigationSideBar(
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit
) {
    Column(
        modifier = Modifier
            .width(70.dp)
            .fillMaxHeight()
            .background(Color(0xFF1E1E1E))
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. Проекты
        val projectsColor = if (currentScreen == AppScreen.PROJECTS) Color.White else Color(0xFF666666)
        TextButton(
            onClick = { onScreenSelected(AppScreen.PROJECTS) },
            colors = ButtonDefaults.textButtonColors(contentColor = projectsColor)
        ) {
            Text(text = "📁", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        // 2. Заметки
        val notesColor = if (currentScreen == AppScreen.NOTES) Color.White else Color(0xFF666666)
        TextButton(
            onClick = { onScreenSelected(AppScreen.NOTES) },
            colors = ButtonDefaults.textButtonColors(contentColor = notesColor)
        ) {
            Text(text = "📝", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        // 2. Канбан-доска
        val kanbanColor = if (currentScreen == AppScreen.KANBAN) Color.White else Color(0xFF666666)
        TextButton(
            onClick = { onScreenSelected(AppScreen.KANBAN) },
            colors = ButtonDefaults.textButtonColors(contentColor = kanbanColor)
        ) {
            Text(text = "📋", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        // 3. Скрипты
        val scriptsColor = if (currentScreen == AppScreen.SCRIPTS) Color.White else Color(0xFF666666)
        TextButton(
            onClick = { onScreenSelected(AppScreen.SCRIPTS) },
            colors = ButtonDefaults.textButtonColors(contentColor = scriptsColor)
        ) {
            Text(text = "⚡", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        // Пружина, выталкивающая шестеренку вниз
        Spacer(modifier = Modifier.weight(1f))

        // 4.
        val settingsColor = if (currentScreen == AppScreen.SETTINGS) Color.White else Color(0xFF666666)
        TextButton(
            onClick = { onScreenSelected(AppScreen.SETTINGS) },
            colors = ButtonDefaults.textButtonColors(contentColor = settingsColor)
        ) {
            Text(text = "🛠️", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}
