package com.example.domain.di

import com.example.data.repository.FormatTextRepository
import com.example.data.repository.NoteItemRepository
import com.example.data.repository.NoteRepository
import com.example.data.repository.PreferencesRepository
import com.example.domain.usecase.detail.DeleteFormatTextUseCase
import com.example.domain.usecase.detail.DeleteNoteItemUseCase
import com.example.domain.usecase.detail.DeleteNoteUseCase
import com.example.domain.usecase.detail.GetNoteDetailUseCase
import com.example.domain.usecase.detail.InsertNoteUseCase
import com.example.domain.usecase.detail.UpdateNoteUseCase
import com.example.domain.usecase.list.DeleteNotesUseCase
import com.example.domain.usecase.list.GetNotesUseCase
import com.example.domain.usecase.list.UpdateNotesUseCase
import com.example.domain.usecase.preferences.GetIsDarkThemeUseCase
import com.example.domain.usecase.preferences.GetLanguageUseCase
import com.example.domain.usecase.preferences.SetIsDarkThemeUseCase
import com.example.domain.usecase.preferences.SetLanguageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    fun providesGetNotesUseCase(
        repository: NoteRepository,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): GetNotesUseCase {
        return GetNotesUseCase(repository, dispatcher)
    }

    @Provides
    fun providesDeleteNotesUseCase(
        repository: NoteRepository,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): DeleteNotesUseCase {
        return DeleteNotesUseCase(repository, dispatcher)
    }

    @Provides
    fun providesUpdateNotesUseCase(
        repository: NoteRepository,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): UpdateNotesUseCase {
        return UpdateNotesUseCase(repository, dispatcher)
    }

    @Provides
    fun providesGetNoteDetailUseCase(
        repository: NoteRepository,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): GetNoteDetailUseCase {
        return GetNoteDetailUseCase(repository, dispatcher)
    }

    @Provides
    fun providesInsertNoteUseCase(
        repository: NoteRepository,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): InsertNoteUseCase {
        return InsertNoteUseCase(repository, dispatcher)
    }

    @Provides
    fun providesUpdateNoteUseCase(
        repository: NoteRepository,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): UpdateNoteUseCase {
        return UpdateNoteUseCase(repository, dispatcher)
    }

    @Provides
    fun providesDeleteNoteUseCase(
        repository: NoteRepository,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): DeleteNoteUseCase {
        return DeleteNoteUseCase(repository, dispatcher)
    }

    @Provides
    fun providesDeleteFormatTextUseCase(
        repository: FormatTextRepository,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): DeleteFormatTextUseCase {
        return DeleteFormatTextUseCase(repository, dispatcher)
    }

    @Provides
    fun providesDeleteNoteItemUseCase(
        repository: NoteItemRepository,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): DeleteNoteItemUseCase {
        return DeleteNoteItemUseCase(repository, dispatcher)
    }

    @Provides
    fun provideSetLanguageUseCase(
        repository: PreferencesRepository,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): SetLanguageUseCase = SetLanguageUseCase(repository, dispatcher)

    @Provides
    fun provideGetLanguageUseCase(
        repository: PreferencesRepository,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): GetLanguageUseCase = GetLanguageUseCase(repository, dispatcher)

    @Provides
    fun provideGetIsDarkThemeUseCase(
        repository: PreferencesRepository,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): GetIsDarkThemeUseCase = GetIsDarkThemeUseCase(repository, dispatcher)

    @Provides
    fun provideSetIsDarkThemeUseCase(
        repository: PreferencesRepository,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): SetIsDarkThemeUseCase = SetIsDarkThemeUseCase(repository, dispatcher)
}