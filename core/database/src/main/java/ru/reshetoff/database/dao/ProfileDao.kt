package ru.reshetoff.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.reshetoff.database.entity.ProfileEntity
import java.util.UUID

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profileEntity: ProfileEntity)

    @Update
    suspend fun updateProfile(profileEntity: ProfileEntity)

    @Query("SELECT * FROM profiles WHERE id = :userId")
    fun getUserById(userId: UUID): Flow<ProfileEntity?>

    @Query("SELECT * FROM profiles WHERE id = :userId")
    suspend fun getUserByIdSync(userId: UUID): ProfileEntity?

    @Delete
    suspend fun deleteProfile(profileEntity: ProfileEntity)
}