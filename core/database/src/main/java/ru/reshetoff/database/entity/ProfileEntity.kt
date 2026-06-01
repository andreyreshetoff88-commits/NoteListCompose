package ru.reshetoff.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey val id: UUID,
    val displayName: String,
    val phoneNumber: String,
    val email: String,
    val isVerified: Boolean
)
