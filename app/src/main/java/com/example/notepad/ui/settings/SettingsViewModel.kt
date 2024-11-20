package com.example.notepad.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.preferences.SetIsDarkThemeUseCase
import com.example.domain.usecase.preferences.SetLanguageUseCase
import com.example.notepad.di.MainDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @MainDispatcher private val dispatcher: CoroutineDispatcher,
    private val setIsDarkThemeUseCase: SetIsDarkThemeUseCase,
    private val setLanguageUseCase: SetLanguageUseCase
) : ViewModel() {

    fun setIsDarkTheme(isDarkTheme: Boolean) {
        viewModelScope.launch(dispatcher) { setIsDarkThemeUseCase(isDarkTheme) }
    }

    fun setLanguage(languageKey: String) {
        viewModelScope.launch(dispatcher) { setLanguageUseCase(languageKey) }
    }
}