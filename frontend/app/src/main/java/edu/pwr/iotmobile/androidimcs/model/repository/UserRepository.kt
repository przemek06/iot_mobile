package edu.pwr.iotmobile.androidimcs.model.repository

import edu.pwr.iotmobile.androidimcs.data.dto.PasswordBody
import edu.pwr.iotmobile.androidimcs.data.dto.UserDto
import edu.pwr.iotmobile.androidimcs.data.dto.UserInfoDto

interface UserRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(userDto: UserDto): Result<UserDto>
    suspend fun getUserInfoById(id: Int): Result<UserInfoDto>
    suspend fun getAllUserInfo(): Result<List<UserInfoDto>>
    suspend fun verifyUser(code: String): Result<Unit>
    suspend fun sendResetPasswordEmail(email: String): Result<Unit>
    suspend fun resetPassword(email: String, code: String, passwordBody: PasswordBody): Result<UserDto>
    suspend fun getActiveUserInfo(): Result<UserInfoDto>
    suspend fun updateActiveUser(userDto: UserDto): Result<UserDto>
    suspend fun updateActiveUserPassword(passwordBody: PasswordBody): Result<UserDto>
    suspend fun deleteActiveUser(): Result<Unit>
    suspend fun deleteUserById(id: Int): Result<Unit>
    suspend fun updateUserRole(id: Int, role: String): Result<UserInfoDto>
    suspend fun toggleUserBlocked(id: Int): Result<UserInfoDto>
}