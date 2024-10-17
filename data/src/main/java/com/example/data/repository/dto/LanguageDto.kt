package com.example.data.repository.dto

import com.example.model.enums.Language
import javax.inject.Inject

class LanguageDto @Inject constructor() {
    fun toDomain(language: String) = when (language) {
        Language.EN.key -> Language.EN
        Language.SP.key -> Language.SP
        Language.FR.key -> Language.FR
        Language.IT.key -> Language.IT
        Language.DE.key -> Language.DE
        Language.PT.key -> Language.PT
        else -> Language.EN
    }
}