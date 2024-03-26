package com.kLoc.ktquizz1.di


import com.kLoc.ktquizz1.firestoredb.repository.FirestoreDbRepositoryImpl
import com.kLoc.ktquizz1.firestoredb.repository.FirestoreRepository
import com.kLoc.ktquizz1.firestoredb.repository.QuestionRepository
import com.kLoc.ktquizz1.firestoredb.repository.QuestionRepositoryImpl
import com.kLoc.ktquizz1.firestoredb.repository.SeriesRepository
import com.kLoc.ktquizz1.firestoredb.repository.SeriesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule
{
    @Binds
    abstract fun providesFirestoreRepository(
        repo: FirestoreDbRepositoryImpl
    ): FirestoreRepository

    @Binds
    abstract fun providesSeriesRepository(
        repo: SeriesRepositoryImpl
    ): SeriesRepository
    @Binds
    abstract fun providesQuestionRepository(
        repo: QuestionRepositoryImpl
    ): QuestionRepository
}