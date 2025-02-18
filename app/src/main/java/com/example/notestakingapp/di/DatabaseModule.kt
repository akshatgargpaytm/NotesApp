package com.example.notestakingapp.di
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext

import android.content.Context
import androidx.room.Room
import com.example.notestakingapp.data.local.NoteDatabase
import com.example.notestakingapp.data.local.dao.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule{
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(
                context,
                NoteDatabase::class.java,
                "note_database"
            ).fallbackToDestructiveMigration(false)
            .build()
    }
    fun provideNoteDao(database: NoteDatabase): NoteDao {
        return database.noteDao()
    }


}