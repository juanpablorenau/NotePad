package com.example.notepad.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.preferences.SetIsDarkThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main,
    private val setIsDarkThemeUseCase: SetIsDarkThemeUseCase,
) : ViewModel() {

    fun setIsDarkTheme(isDarkTheme: Boolean) {
        viewModelScope.launch(dispatcher) { setIsDarkThemeUseCase(isDarkTheme) }
    }
}