package com.example.devhub

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

import com.example.devhub.ui.components.screens.MainWorkspaceScreen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "DevHub Workspace"
    ) {
        MainWorkspaceScreen()
    }
}
