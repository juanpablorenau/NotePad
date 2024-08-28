package com.example.model.entities

open class NoteItem(open val id: String = "")

data class NoteCheckBox(
    override val id: String = "",
    val text: String = "",
    val isChecked: Boolean = false,
) : NoteItem(id)

data class NoteTextField(
    override val id: String = "",
    val text: String = "",
) : NoteItem(id)