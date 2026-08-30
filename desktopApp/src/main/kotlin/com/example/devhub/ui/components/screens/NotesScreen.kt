package com.example.devhub.ui.components.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devhub.data.LocalProject
import com.example.devhub.data.ProjectNotesRepository
import kotlinx.coroutines.delay

@Composable
fun NotesScreen(
    selectedProject: LocalProject?
) {
    // Если проект не выбран в левой панели, показываем красивую заглушку
    if (selectedProject == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E1E1E)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Выберите проект в списке слева, чтобы открыть заметки",
                color = Color.Gray,
                fontSize = 16.sp
            )
        }
        return
    }

    val projectPath = selectedProject.path

    // Загружаем заметку и подписываем её на изменение path
    var noteText by remember(projectPath) {
        mutableStateOf(ProjectNotesRepository.loadNote(projectPath))
    }

    // Автоматический фокус на поле ввода при открытии экрана
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(projectPath) {
        focusRequester.requestFocus()
    }

    // Умное сохранение
    // Запускает таймер на 500мс. Если пользователь нажал новую букву — таймер сбрасывается.
    // Запись на диск сработает только тогда, когда сделана паузу в наборе.
    LaunchedEffect(noteText) {
        delay(500)
        ProjectNotesRepository.saveNote(projectPath, noteText)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        // Информационный заголовок сверху
        Text(
            text = "Проект: ${selectedProject.name} — Редактирование devhub_notes.md",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Полноэкранный TextField без рамок и подложек
        BasicTextField(
            value = noteText,
            onValueChange = { newText -> noteText = newText },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .focusRequester(focusRequester),
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                lineHeight = 24.sp
            ),
            cursorBrush = SolidColor(Color(0xFF8A2BE2)),
            decorationBox = { innerTextField ->
                if (noteText.isEmpty()) {
                    Text(
                        text = "Начните писать здесь важные мысли по проекту...\nФайл автоматически сохранится в корне папки.",
                        color = Color(0xFF555555),
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                }
                innerTextField()
            }
        )
    }
}
