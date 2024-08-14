package com.example.domain.di

import com.example.data.repository.NoteRepository
import com.example.domain.usecase.DeleteNoteUseCase
import com.example.domain.usecase.GetNoteDetailUseCase
import com.example.domain.usecase.GetNotesUseCase
import com.example.domain.usecase.InsertNoteUseCase
import com.example.domain.usecase.UpdateNoteUseCase
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
}