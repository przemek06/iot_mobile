package edu.pwr.iotmobile.androidimcs.data

data class User(
    val id: String,
    val name: String,
    val surname: String,
    val email: String,
    val role: UserRole
)

enum class UserRole {
    View,
    Modify,
    Admin
}