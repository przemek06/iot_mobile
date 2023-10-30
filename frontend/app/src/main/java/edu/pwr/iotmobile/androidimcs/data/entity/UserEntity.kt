package edu.pwr.iotmobile.androidimcs.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: Int,
    val email: String,
    val role: String,
    val name: String,
    val isBlocked: Boolean,
    val isActive: Boolean
)
