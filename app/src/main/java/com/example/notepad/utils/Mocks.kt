package com.example.notepad.utils

import com.example.model.entities.Author
import com.example.model.entities.Note
import java.util.Date

val mockNoteList by lazy {
    listOf(
        mockNote.copy(
            title = "Reunión con el equipo de desarrollo",
            content = "No olvides la reunión con el equipo de desarrollo programada para mañana a las 10:00 AM. Discutiremos los avances del proyecto y planificaremos las siguientes etapas.",
            isPinned = true,
            color = "#FFB6C1",
            isChecked = true
        ),
        mockNote.copy(
            title = "Lista de Tareas",
            content = "1. Terminar el reporte financiero\n2. Llamar al proveedor\n3. Enviar correos pendientes\n4. Revisar el documento de especificaciones del proyecto",
            color = "#BFD3C1"
        ),
        mockNote.copy(
            title = "Ideas para el nuevo proyecto",
            content = "1. Implementar una función de búsqueda avanzada\n2. Agregar soporte para múltiples idiomas\n3. Mejorar la experiencia del usuario en dispositivos móviles",
            color = "#77DD77",
            isChecked = true
            ),
        mockNote.copy(
            title = "Cita del día",
            content = "\"El único modo de hacer un gran trabajo es amar lo que haces.\" - Steve Jobs",
            color = "#FDFD96"
        ),
        mockNote.copy(title = "5", isPinned = true),
        mockNote.copy(title = "6")
    )
}

val mockNote by lazy {
    Note(
        title = "Meeting Notes",
        content = "Here are the notes from the meeting.",
        author = Author(),
        createdDate = Date(),
        lastModifiedDate = Date(),
        category = "Personal",
        color = "#fff138",
        isPinned = false,
        isFavorite = true,
        isArchived = true,
        isLocked = true,
    )
}