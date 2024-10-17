package com.example.notepad.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.preferences.GetIsDarkThemeUseCase
import com.example.domain.usecase.preferences.GetLanguageUseCase
import com.example.model.enums.Language
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main,
    private val getIsDarkThemeUseCase: GetIsDarkThemeUseCase,
    private val getLanguageUseCase: GetLanguageUseCase
) : ViewModel() {

    private val _isDarkTheme = MutableSharedFlow<Boolean>(replay = 1)
    val isDarkTheme: SharedFlow<Boolean> = _isDarkTheme.asSharedFlow()

    private val _language = MutableSharedFlow<Language>(replay = 1)
    val language: SharedFlow<Language> = _language.asSharedFlow()

    init {
        getIsDarkTheme()
        getLanguage()
    }

    private fun getIsDarkTheme() {
        viewModelScope.launch(dispatcher) {
            getIsDarkThemeUseCase().collect { isDark -> _isDarkTheme.emit(isDark) }
        }
    }

    private fun getLanguage() {
        viewModelScope.launch(dispatcher) {
            getLanguageUseCase().collect { language -> _language.emit(language) }
        }
    }
}
