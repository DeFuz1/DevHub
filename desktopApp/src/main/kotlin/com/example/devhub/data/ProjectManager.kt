package com.example.devhub.data

import java.io.File

data class LocalProject(
    val name: String,
    val path: String,
    val technologies: String,
    val dateAdded: String
)

object ProjectManager {
    // Окна выбора и логика заметок
    fun chooseFolderDialog(): String? {
        val chooser = javax.swing.JFileChooser().apply {
            fileSelectionMode = javax.swing.JFileChooser.DIRECTORIES_ONLY
            dialogTitle = "Выберите папку вашего проекта"
        }
        val result = chooser.showOpenDialog(null)
        return if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
            chooser.selectedFile.absolutePath
        } else {
            null
        }
    }
}
