package edu.pwr.iotmobile.androidimcs.model.datasource.remote

import edu.pwr.iotmobile.androidimcs.data.dto.PasswordBody
import edu.pwr.iotmobile.androidimcs.data.dto.UserDto
import edu.pwr.iotmobile.androidimcs.data.dto.UserInfoDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserRemoteDataSource {

    // Login/register endpoints

    @POST("/login")
    @FormUrlEncoded
    suspend fun loginUser(
        @FieldMap params: Map<String,String>
    ): Response<ResponseBody> // nie zwraca usera

    @POST("/anon/users")
    suspend fun registerUser(
        @Body userDto: UserDto
    ): Response<UserDto>

    @GET("/anon/users/info/{id}")
    suspend fun getUserInfoById(
        @Path("id") id: Int
    ): Response<UserInfoDto>

    @GET("/anon/users/info")
    suspend fun getAllUserInfo(): Response<List<UserInfoDto>>

    @POST("/anon/users/verify/{code}")
    suspend fun verifyUser(
        @Path("code") code: String
    ): Response<ResponseBody>

    @POST("/anon/users/reset/{email}")
    suspend fun sendResetPasswordEmail(
        @Path("email") email: String
    ): Response<ResponseBody>

    @POST("/anon/users/reset/{email}/{code}")
    suspend fun resetPassword(
        @Path("email") email: String,
        @Path("code") code: String,
        @Body password: PasswordBody
    ): Response<UserDto>

    // User account management endpoints

    @GET("/user/users/info")
    suspend fun getActiveUserInfo(): Response<UserInfoDto>

    @PUT("/user/users")
    suspend fun updateActiveUser(
        @Body userDto: UserDto
    ): Response<UserDto>

    @PUT("/user/users/password")
    suspend fun updateActiveUserPassword(
        @Body password: PasswordBody
    ): Response<UserDto>

    @DELETE("/user/users")
    suspend fun deleteActiveUser(): Response<ResponseBody>

    // Admin endpoints

    @DELETE("/admin/users/{id}")
    suspend fun deleteUserById(
        @Path("id") id: Int
    ): Response<ResponseBody>

    @PUT("/admin/users/role/{id}/{role}")
    suspend fun updateUserRole(
        @Path("id") id: Int,
        @Path("role") role: String
    ): Response<UserInfoDto>

    @PUT("/admin/users/block/toggle/{id}")
    suspend fun toggleUserBlocked(
        @Path("id") id: Int
    ): Response<UserInfoDto>
}