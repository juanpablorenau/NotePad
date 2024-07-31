package com.example.model.entities

import java.util.UUID

data class Author(
    val name: String = "",
) {
    val id: String = UUID.randomUUID().toString()
}
