package ru.reshetoff.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.reshetoff.database.dao.ProfileDao
import ru.reshetoff.database.entity.ProfileEntity

@Database(
    [
        ProfileEntity::class
    ],
    version = 1
)
abstract class NoteListDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
}