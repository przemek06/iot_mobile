package edu.pwr.iotmobile.androidimcs.data

data class User(
    val id: String,
    val displayName: String,
    val email: String,
    val role: UserRole
)

enum class UserRole {
    Normal,
    Admin
}

enum class UserProjectRole {
    View,
    Modify,
    Admin
}