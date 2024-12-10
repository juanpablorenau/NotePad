package com.example.notepad.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.preferences.GetDrawerItemIndexUseCase
import com.example.domain.usecase.preferences.GetIsDarkThemeUseCase
import com.example.domain.usecase.preferences.GetLanguageUseCase
import com.example.domain.usecase.preferences.SetDrawerItemIndexUseCase
import com.example.model.enums.Language
import com.example.notepad.di.MainDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @MainDispatcher private val dispatcher: CoroutineDispatcher,
    private val getIsDarkThemeUseCase: GetIsDarkThemeUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val getDrawerItemIndexUseCase: GetDrawerItemIndexUseCase,
    private val setDrawerItemIndexUseCase: SetDrawerItemIndexUseCase
) : ViewModel() {

    private val _isDarkTheme = MutableSharedFlow<Boolean>(replay = 1)
    val isDarkTheme: SharedFlow<Boolean> = _isDarkTheme.asSharedFlow()

    private val _language = MutableSharedFlow<Language>(replay = 1)
    val language: SharedFlow<Language> = _language.asSharedFlow()

    private val _drawerItemIndex = MutableSharedFlow<Int>(replay = 1)
    val drawerItemIndex: SharedFlow<Int> = _drawerItemIndex.asSharedFlow()

    init {
        getIsDarkTheme()
        getLanguage()
        getDrawerItemIndex()
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

    private fun getDrawerItemIndex() {
        viewModelScope.launch(dispatcher) {
            getDrawerItemIndexUseCase().collect { index -> _drawerItemIndex.emit(index) }
        }
    }

    fun setDrawerItemIndex(index: Int) {
        viewModelScope.launch(dispatcher) { setDrawerItemIndexUseCase(index) }
    }
}
