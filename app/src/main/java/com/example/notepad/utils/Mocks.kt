package com.example.notepad.utils

import com.example.model.entities.Author
import com.example.model.entities.Note
import java.util.Date
import com.example.model.entities.Color as AppColor

val mockNoteList by lazy {
    listOf(
        mockNote.copy(
            id = "1",
            title = "Lista de Tareas",
            content = "1. Terminar el reporte financiero\n2. Llamar al proveedor\n3. Enviar correos pendientes\n4. Revisar el documento de especificaciones del proyecto",
        ),
        mockNote.copy(
            id = "2",
            title = "Ideas para el nuevo proyecto",
            content = "1. Implementar una función de búsqueda avanzada\n2. Agregar soporte para múltiples idiomas\n3. Mejorar la experiencia del usuario en dispositivos móviles",
        ),
        mockNote.copy(
            id = "3",
            title = "Cita del día",
            content = "\"El único modo de hacer un gran trabajo es amar lo que haces.\" - Steve Jobs",
        ),
        mockNote.copy(
            id = "4",
            title = "Reunión con el equipo de desarrollo",
            content = "No olvides la reunión con el equipo de desarrollo programada para mañana a las 10:00 AM. Discutiremos los avances del proyecto y planificaremos las siguientes etapas.",
            isPinned = true,
        ),
        mockNote.copy(
            id = "5",
            title = "Lista de compras",
            content = "1. Leche\n2. Pan\n3. Huevos\n4. Café\n5. Frutas",
            isPinned = false,
        ),
        mockNote.copy(
            id = "6",
            title = "Ideas de proyectos",
            content = "1. Aplicación de seguimiento de hábitos\n2. Blog personal\n3. Plataforma de aprendizaje en línea",
            isPinned = false,
        ),
        mockNote.copy(
            id = "7",
            title = "Cita con el dentista",
            content = "Cita con el dentista programada para el viernes a las 3:00 PM.",
            isPinned = false,
        ),
        mockNote.copy(
            id = "8",
            title = "Notas de la clase de Historia",
            content = "Notas importantes sobre la Revolución Francesa y sus impactos.",
            isPinned = false,
        ),
        mockNote.copy(
            id = "9",
            title = "Metas personales",
            content = "1. Leer 20 libros este año\n2. Hacer ejercicio al menos 4 veces a la semana\n3. Aprender un nuevo idioma",
            isPinned = true,
        ),
        mockNote.copy(
            id = "10",
            title = "Eventos del mes",
            content = "1. Cumpleaños de María - 12 de agosto\n2. Reunión familiar - 20 de agosto",
            isPinned = false,
        ),
        mockNote.copy(
            id = "11",
            title = "Recordatorios de trabajo",
            content = "Enviar el informe semanal antes del jueves.\nRevisar los documentos del cliente.",
            isPinned = false,
        ),
        mockNote.copy(
            id = "12",
            title = "Viaje a la playa",
            content = "Preparar las maletas para el viaje del fin de semana a la playa. No olvidar protector solar y gafas de sol.",
            isPinned = true,
        ),
        mockNote.copy(
            id = "13",
            title = "Receta de pasta",
            content = "Ingredientes:\n- Pasta\n- Tomates\n- Albahaca\n- Ajo\n- Aceite de oliva\nInstrucciones: Cocinar la pasta y mezclar con los ingredientes restantes.",
            isPinned = false,
        )
    )
}

val mockNote =
    Note(
        title = "Meeting Notes",
        content = "Here are the notes from the meeting.",
        author = Author(),
        createdDate = Date(),
        lastModifiedDate = Date(),
        category = "Personal",
        lightColor = AppColor.PALE_YELLOW.lightColor,
        darkColor = AppColor.PALE_YELLOW.darkColor,
        isPinned = false,
        isFavorite = true,
        isArchived = true,
        isLocked = true,
    )

