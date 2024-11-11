package com.example.domain.di

import com.example.data.repository.FormatTextRepository
import com.example.data.repository.NoteRepository
import com.example.domain.usecase.detail.DeleteFormatTextUseCase
import com.example.domain.usecase.detail.DeleteNoteUseCase
import com.example.domain.usecase.detail.GetNoteDetailUseCase
import com.example.domain.usecase.detail.InsertNoteUseCase
import com.example.domain.usecase.detail.UpdateNoteUseCase
import com.example.domain.usecase.list.DeleteNotesUseCase
import com.example.domain.usecase.list.GetNotesUseCase
import com.example.domain.usecase.list.UpdateNotesUseCase
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
        dispatcher: CoroutineDispatcher,
    ): GetNotesUseCase {
        return GetNotesUseCase(repository, dispatcher)
    }

    @Provides
    fun providesGetNoteDetailUseCase(
        repository: NoteRepository,
        dispatcher: CoroutineDispatcher,
    ): GetNoteDetailUseCase {
        return GetNoteDetailUseCase(repository, dispatcher)
    }

    @Provides
    fun providesInsertNoteUseCase(
        repository: NoteRepository,
        dispatcher: CoroutineDispatcher,
    ): InsertNoteUseCase {
        return InsertNoteUseCase(repository, dispatcher)
    }

    @Provides
    fun providesUpdateNoteUseCase(
        repository: NoteRepository,
        dispatcher: CoroutineDispatcher,
    ): UpdateNoteUseCase {
        return UpdateNoteUseCase(repository, dispatcher)
    }

    @Provides
    fun providesDeleteNoteUseCase(
        repository: NoteRepository,
        dispatcher: CoroutineDispatcher,
    ): DeleteNoteUseCase {
        return DeleteNoteUseCase(repository, dispatcher)
    }

    @Provides
    fun providesDeleteNotesUseCase(
        repository: NoteRepository,
        dispatcher: CoroutineDispatcher,
    ): DeleteNotesUseCase {
        return DeleteNotesUseCase(repository, dispatcher)
    }

    @Provides
    fun providesUpdateNotesUseCase(
        repository: NoteRepository,
        dispatcher: CoroutineDispatcher,
    ): UpdateNotesUseCase {
        return UpdateNotesUseCase(repository, dispatcher)
    }

    @Provides
    fun providesDeleteFormatTextUseCase(
        repository: FormatTextRepository,
        dispatcher: CoroutineDispatcher,
    ): DeleteFormatTextUseCase {
        return DeleteFormatTextUseCase(repository, dispatcher)
    }
}