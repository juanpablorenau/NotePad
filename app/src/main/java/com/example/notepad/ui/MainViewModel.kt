package com.example.notepad.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.preferences.GetIsDarkThemeUseCase
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
) : ViewModel() {

    private val _isDarkTheme = MutableSharedFlow<Boolean>(replay = 1)
    val isDarkTheme: SharedFlow<Boolean> = _isDarkTheme.asSharedFlow()

    init {
        getIsDarkTheme()
    }

    private fun getIsDarkTheme() {
        viewModelScope.launch(dispatcher) {
            getIsDarkThemeUseCase().collect { isDark -> _isDarkTheme.emit(isDark) }
        }
    }
}
