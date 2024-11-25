package com.example.notepad.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
internal object DispatchersModule {

    @Provides
    @MainDispatcher
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher
