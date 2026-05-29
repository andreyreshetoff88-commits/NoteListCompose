package ru.reshetoff.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Provides
    @Singleton
    fun provideNoteListDatabase(@ApplicationContext context: Context): NoteListDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = NoteListDatabase::class.java,
            name = "notelist_db"
        ).fallbackToDestructiveMigration(true).build()
    }

    @Provides
    @Singleton
    fun provideProfileDao(db: NoteListDatabase) = db.profileDao()
}