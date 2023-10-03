package edu.pwr.iotmobile.androidimcs.data.dto

data class RegisterDto(
    val email: String,
    val displayName: String,
    val password: String,
    val confirmPassword: String
)
