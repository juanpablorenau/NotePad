package com.example.model.entities

import java.util.UUID

open class NoteItem(
    val id: String = UUID.randomUUID().toString(),
)

data class NoteCheckBox(
    val text: String,
    val isChecked: Boolean = false,
) : NoteItem()

data class NoteTextField(
    val text: String,
) : NoteItem()
