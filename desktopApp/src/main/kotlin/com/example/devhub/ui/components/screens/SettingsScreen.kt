package com.example.devhub.ui.components.screens

import androidx.compose.foundation.background
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
import com.example.devhub.data.ProjectConfigRepository

@Composable
fun SettingsScreen(
    onResetWorkspace: () -> Unit // Экшен для сброса состояния верхнего уровня
) {
    var studioPath by remember { mutableStateOf(ProjectConfigRepository.getCustomStudioPath() ?: "") }
    var ideaPath by remember { mutableStateOf(ProjectConfigRepository.getCustomIdeaPath() ?: "") }
    var vsCodePath by remember { mutableStateOf(ProjectConfigRepository.getCustomVsCodePath() ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(text = "🛠️ Настройки путей IDE", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "Укажите прямые пути к исполняемым (.exe) файлам сред разработки, если они установлены в кастомные папки (например, на диск D:\\). " +
                    "Экран скриптов прочитает эти пути для запуска.",
            color = Color.Gray, fontSize = 14.sp, lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        IdePathInputField("Путь к Android Studio (studio64.exe):", studioPath, { studioPath = it }, "Пример: D:\\IDEs\\Android Studio\\bin\\studio64.exe")
        IdePathInputField("Путь к IntelliJ IDEA (idea64.exe):", ideaPath, { ideaPath = it }, "Пример: D:\\IDEs\\IntelliJ IDEA\\bin\\idea64.exe")
        IdePathInputField("Путь к Visual Studio Code (Code.exe):", vsCodePath, { vsCodePath = it }, "Пример: D:\\IDEs\\VSCode\\Code.exe")

        Spacer(modifier = Modifier.height(16.dp))

        // Ряд с кнопками управления
        Row(
            modifier = Modifier.fillMaxWidth(),
            // Добавляем выравнивание по центру вертикали, чтобы кнопки стояли ровно друг напротив друга
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Кнопка сохранения
            Button(
                onClick = { ProjectConfigRepository.saveCustomPaths(studioPath.trim(), ideaPath.trim(), vsCodePath.trim()) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF8A2BE2), contentColor = Color.White),
                modifier = Modifier.width(150.dp).height(48.dp)
            ) {
                Text("Сохранить", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            // Кнопка полной очистки воркспейса
            Button(
                onClick = {
                    ProjectConfigRepository.clearAllWorkspaceAndSettings()
                    studioPath = ""
                    ideaPath = ""
                    vsCodePath = ""
                    onResetWorkspace()
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFE53935), contentColor = Color.White),
                modifier = Modifier
                    .width(220.dp)
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = "💥 Полная очистка воркспейса",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 2,
                    lineHeight = 18.sp
                )
            }
        }


        Spacer(modifier = Modifier.weight(1f))
        Text(text = "DevHub Workspace v0.8.0", color = Color(0xFF444444), fontSize = 12.sp)
    }
}

// Переиспользуемый стильный компонент поля ввода путей без рамок
@Composable
fun IdePathInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            color = Color(0xFFBBBBBB),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )

        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color(0xFF555555)) },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .background(Color(0xFF2D2D2D), shape = RoundedCornerShape(8.dp)),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 14.sp),
            colors = androidx.compose.material.TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}
