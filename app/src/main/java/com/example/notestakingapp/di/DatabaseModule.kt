package com.example.notestakingapp.di

import android.content.Context
import androidx.room.Room
import com.example.notestakingapp.data.local.NoteDatabase
import com.example.notestakingapp.data.local.dao.NoteDao
import com.example.notestakingapp.data.remote.api.NoteApi
import com.example.notestakingapp.data.repository.NoteRepository
import com.example.notestakingapp.data.repository.NoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            "note_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideNoteDao(database: NoteDatabase): NoteDao {
        return database.noteDao()
    }
}
