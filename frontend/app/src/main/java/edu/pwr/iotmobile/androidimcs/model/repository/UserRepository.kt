package edu.pwr.iotmobile.androidimcs.model.repository

import edu.pwr.iotmobile.androidimcs.data.User
import edu.pwr.iotmobile.androidimcs.data.dto.PasswordBody
import edu.pwr.iotmobile.androidimcs.data.dto.UserDto
import edu.pwr.iotmobile.androidimcs.data.dto.UserInfoDto
import edu.pwr.iotmobile.androidimcs.data.result.ActivateAccountResult
import edu.pwr.iotmobile.androidimcs.data.result.ForgotPasswordResult
import edu.pwr.iotmobile.androidimcs.data.result.LoginUserResult
import edu.pwr.iotmobile.androidimcs.data.result.RegisterUserResult
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(email: String, password: String): LoginUserResult
    suspend fun logout(): Result<Unit>
    suspend fun register(userDto: UserDto): RegisterUserResult
    // TODO: suspend fun sendActivateCodeEmail(email: String): Result<Unit>
    /**
     * Returns User if logged in, null otherwise.
     */
    suspend fun getLoggedInUser(): Flow<User?>
    suspend fun logOut(): Result<Unit>
    suspend fun getUserInfoById(id: Int): Result<UserInfoDto>
    suspend fun getAllUserInfo(): Result<List<UserInfoDto>>
    suspend fun verifyUser(code: String): ActivateAccountResult
    suspend fun sendResetPasswordEmail(email: String): Result<Unit>
    suspend fun resetPassword(email: String, code: String, passwordBody: PasswordBody): ForgotPasswordResult
    suspend fun getActiveUserInfo(): Result<UserInfoDto>
    suspend fun updateActiveUser(userDto: UserDto): Result<UserDto>
    suspend fun updateActiveUserPassword(passwordBody: PasswordBody): Result<UserDto>
    suspend fun deleteActiveUser(): Result<Unit>
    suspend fun deleteUserById(id: Int): Result<Unit>
    suspend fun updateUserRole(id: Int, role: String): Result<UserInfoDto>
    suspend fun toggleUserBlocked(id: Int): Result<UserInfoDto>
}