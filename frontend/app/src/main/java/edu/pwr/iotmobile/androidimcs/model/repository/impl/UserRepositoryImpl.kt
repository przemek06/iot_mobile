package edu.pwr.iotmobile.androidimcs.model.repository.impl

import android.util.Log
import edu.pwr.iotmobile.androidimcs.data.User
import edu.pwr.iotmobile.androidimcs.data.User.Companion.toUser
import edu.pwr.iotmobile.androidimcs.data.dto.EmailDto
import edu.pwr.iotmobile.androidimcs.data.dto.PasswordBody
import edu.pwr.iotmobile.androidimcs.data.dto.UserDto
import edu.pwr.iotmobile.androidimcs.data.dto.UserInfoDto
import edu.pwr.iotmobile.androidimcs.data.result.ActivateAccountResult
import edu.pwr.iotmobile.androidimcs.data.result.ForgotPasswordResult
import edu.pwr.iotmobile.androidimcs.data.result.LoginUserResult
import edu.pwr.iotmobile.androidimcs.data.result.RegisterUserResult
import edu.pwr.iotmobile.androidimcs.model.datasource.local.UserLocalDataSource
import edu.pwr.iotmobile.androidimcs.model.datasource.local.UserSessionLocalDataSource
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.UserRemoteDataSource
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val TAG = "UserRepo"

class UserRepositoryImpl(
    private val remoteDataSource: UserRemoteDataSource,
    private val userSessionLocalDataSource: UserSessionLocalDataSource,
    private val userLocalDataSource: UserLocalDataSource
) : UserRepository {
    override suspend fun login(email: String, password: String): LoginUserResult {
        val params = mapOf(
            "username" to email,
            "password" to password
        )
        val response = remoteDataSource.loginUser(params)
        val resultCode = response.code()
        Log.d(TAG, "login result code: $resultCode")

        return when (resultCode) {
            200 -> {
                // Save user session
                val cookie = response.headers()["Set-Cookie"]
                cookie?.let {
                    userSessionLocalDataSource.saveUserSessionCookie(it)
                }

                // Get user info
                val userInfo = getActiveUserInfo().getOrNull() ?: return LoginUserResult.Failure

                // Save user to local db
                try {
                    userLocalDataSource.updateData { store ->
                        store.toBuilder()
                            .setId(userInfo.id)
                            .setEmail(userInfo.email)
                            .setRole(userInfo.role)
                            .setName(userInfo.name)
                            .setIsBlocked(userInfo.isBlocked)
                            .setIsActive(userInfo.isActive)
                            .build()
                    }
                } catch (e: Exception) {
                    return LoginUserResult.Failure
                }

                LoginUserResult.Success
            }

            401 -> LoginUserResult.UserBanned

            403 -> LoginUserResult.AccountInactive

            else -> LoginUserResult.Failure
        }
    }

    override suspend fun logout(): Result<Unit> {
        val response = remoteDataSource.logoutUser()
        val resultCode = response.code()
        Log.d(TAG, "logout result code: $resultCode")

        return try {
            // Delete user from local db
            userLocalDataSource.updateData { store ->
                store.toBuilder().clear().build()
            }
            // Delete user session
            userSessionLocalDataSource.removeUserSessionCookie()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to remove user session."))
        }
    }

    override suspend fun register(userDto: UserDto): RegisterUserResult {
        val response = remoteDataSource.registerUser(userDto)
        val resultCode = response.code()
        Log.d(TAG, "register result code: $resultCode")
        return when (resultCode) {
            200 -> RegisterUserResult.Success
            409 -> RegisterUserResult.AccountExists
            else -> RegisterUserResult.Failure
        }
    }

    override suspend fun resendVerificationCode(email: String): Result<Unit> {
        val dto = EmailDto(address = email)
        val response = remoteDataSource.resendVerificationCode(dto)
        return if (response.isSuccessful)
            Result.success(Unit)
        else
            Result.failure(Exception("Resend verification code failed"))
    }

    override suspend fun getLoggedInUser(): Flow<User?> =
        userLocalDataSource
            .getData()
            .map { it.toUser() }

    override suspend fun getUserInfoById(id: Int): Result<UserInfoDto> {
        val response = remoteDataSource.getUserInfoById(id)
        val body = response.body()
        return if (response.isSuccessful && body != null)
            Result.success(body)
        else
            Result.failure(Exception("Get user info failed"))
    }

    override suspend fun getAllUserInfo(): List<UserInfoDto> {
        val response = remoteDataSource.getAllUserInfo()
        val body = response.body()
        return if (response.isSuccessful && body != null)
            body
        else
            emptyList()
    }

    override suspend fun verifyUser(code: String): ActivateAccountResult {
        val response = remoteDataSource.verifyUser(code)
        val resultCode = response.code()
        Log.d(TAG, "activate account result code: $resultCode")
        return when (resultCode) {
            200 -> ActivateAccountResult.Success
            400 -> ActivateAccountResult.IncorrectCode
            else -> ActivateAccountResult.Failure
        }
    }

    override suspend fun sendResetPasswordEmail(email: String): Result<Unit> {
        val response = remoteDataSource.sendResetPasswordEmail(email)
        return if (response.isSuccessful)
            Result.success(Unit)
        else
            Result.failure(Exception("Send reset password email failed"))
    }

    override suspend fun resetPassword(
        email: String,
        code: String,
        passwordBody: PasswordBody
    ): ForgotPasswordResult {
        val response = remoteDataSource.resetPassword(email, code, passwordBody)
        val resultCode = response.code()
        Log.d(TAG, "forgot password result code: $resultCode")
        return when (resultCode) {
            200 -> ForgotPasswordResult.Success
            400 -> ForgotPasswordResult.CodeIncorrect
            else -> ForgotPasswordResult.Failure
        }
    }

    override suspend fun getActiveUserInfo(): Result<UserInfoDto> {
        val response = remoteDataSource.getActiveUserInfo()
        val body = response.body()
        return if (response.isSuccessful && body != null)
            Result.success(body)
        else
            Result.failure(Exception("Get active user info failed"))
    }

    override suspend fun updateActiveUser(userDto: UserDto): Result<UserDto> {
        val response = remoteDataSource.updateActiveUser(userDto)
        val body = response.body()
        return if (response.isSuccessful && body != null)
            Result.success(body)
        else
            Result.failure(Exception("Update active user failed"))
    }

    override suspend fun updateActiveUserPassword(passwordBody: PasswordBody): Result<UserDto> {
        val response = remoteDataSource.updateActiveUserPassword(passwordBody)
        val body = response.body()
        return if (response.isSuccessful && body != null)
            Result.success(body)
        else
            Result.failure(Exception("Update active user password failed"))
    }

    override suspend fun deleteActiveUser(): Result<Unit> {
        val response = remoteDataSource.deleteActiveUser()
        return if (response.isSuccessful)
            Result.success(Unit)
        else
            Result.failure(Exception("Delete active user failed"))
    }

    override suspend fun deleteUserById(id: Int): Result<Unit> {
        val response = remoteDataSource.deleteUserById(id)
        return if (response.isSuccessful)
            Result.success(Unit)
        else
            Result.failure(Exception("Delete user by id failed"))
    }

    override suspend fun updateUserRole(id: Int, role: String): Result<UserInfoDto> {
        val response = remoteDataSource.updateUserRole(id, role)
        val body = response.body()
        return if (response.isSuccessful && body != null)
            Result.success(body)
        else
            Result.failure(Exception("Update user role failed"))
    }

    override suspend fun toggleUserBlocked(id: Int): Result<UserInfoDto> {
        val response = remoteDataSource.toggleUserBlocked(id)
        val body = response.body()
        return if (response.isSuccessful && body != null)
            Result.success(body)
        else
            Result.failure(Exception("Toggle user blocked failed"))
    }
}