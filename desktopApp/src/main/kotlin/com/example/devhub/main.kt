package com.example.devhub

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

import com.example.devhub.ui.components.screens.MainWorkspaceScreen
import java.awt.Dimension

fun main() = application {
    val windowState = rememberWindowState(width = 1400.dp, height = 800.dp)
    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "DevHub Workspace"
    ) {
        LaunchedEffect(Unit) {
            window.minimumSize = Dimension(1400, 800)
        }
        MainWorkspaceScreen()
    }
}
