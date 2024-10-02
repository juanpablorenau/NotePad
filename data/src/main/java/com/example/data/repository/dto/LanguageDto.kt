package com.example.data.repository.dto

import com.example.model.entities.Language
import javax.inject.Inject

class LanguageDto @Inject constructor() {
    fun toDomain(language: String) = when (language) {
        Language.EN.name -> Language.EN
        Language.SP.name  -> Language.SP
        else -> Language.EN
    }
}