package com.kloc.ktadmin.di


import com.kloc.ktadmin.firestoredb.repository.FirestoreDbRepositoryImpl
import com.kloc.ktadmin.firestoredb.repository.FirestoreRepository
import com.kloc.ktadmin.firestoredb.repository.QuestionRepository
import com.kloc.ktadmin.firestoredb.repository.QuestionRepositoryImpl
import com.kloc.ktadmin.firestoredb.repository.SeriesRepository
import com.kloc.ktadmin.firestoredb.repository.SeriesRepositoryImpl
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