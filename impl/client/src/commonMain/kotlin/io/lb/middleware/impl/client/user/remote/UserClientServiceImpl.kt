package io.lb.middleware.impl.client.user.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.lb.middleware.common.remote.user.remote.model.LoginRequest
import io.lb.middleware.common.remote.user.remote.model.UpdatePasswordRequest
import io.lb.middleware.common.remote.user.remote.UserClientService
import io.lb.middleware.common.remote.user.remote.model.LoginResult
import io.lb.middleware.common.remote.user.remote.model.UserCreateRequest
import io.lb.middleware.common.remote.user.remote.model.UserResult
import io.lb.middleware.common.remote.user.remote.model.UserUpdateRequest
import io.lb.middleware.common.shared.user.UserException
import io.lb.middleware.impl.client.NetworkConstants
import io.lb.middleware.impl.client.user.remote.model.LoginParameter
import io.lb.middleware.impl.client.user.remote.model.LoginResponse
import io.lb.middleware.impl.client.user.remote.model.UpdatePasswordParameter
import io.lb.middleware.impl.client.user.remote.model.UserCreateParameter
import io.lb.middleware.impl.client.user.remote.model.UserResponse

/**
 * Service for making requests to the middleware server.
 */
class UserClientServiceImpl(
    private val httpClient: HttpClient
) : UserClientService {
    private val baseUrl = NetworkConstants.USER_BASE_URL

    override suspend fun login(data: LoginRequest): LoginResult? {
        val result = httpClient.post {
            url("$baseUrl/api/login")
            contentType(ContentType.Application.Json)
            setBody(
                LoginParameter(
                    email = data.email,
                    password = data.password
                )
            )
        }

        if (result.status != HttpStatusCode.OK) {
            throw UserException(result.bodyAsText())
        }

        return result.body<LoginResponse?>()?.toResult()
    }

    override suspend fun getUserById(token: String, userId: String): UserResult? {
        val result = httpClient.get {
            url("$baseUrl/api/user?userId=$userId")
            contentType(ContentType.Application.Json)
            bearerAuth(token)
        }

        if (result.status != HttpStatusCode.OK) {
            throw UserException(result.bodyAsText())
        }

        return result.body<UserResponse?>()?.toResult()
    }

    override suspend fun signUp(data: UserCreateRequest): String {
        val result = httpClient.post {
            url("$baseUrl/api/signUp")
            contentType(ContentType.Application.Json)
            setBody(
                UserCreateParameter(
                    email = data.email,
                    password = data.password,
                    phone = data.phone,
                    userName = data.userName,
                    profilePictureUrl = data.profilePictureUrl
                )
            )
        }

        if (result.status != HttpStatusCode.Created) {
            throw UserException(result.bodyAsText())
        }

        return result.bodyAsText()
    }

    override suspend fun updateUser(token: String, data: UserUpdateRequest): UserResult? {
        val result = httpClient.put {
            url("$baseUrl/api/updateUser?userId=${data.userId}")
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(
                UserCreateParameter(
                    email = data.email,
                    password = data.password,
                    phone = data.phone,
                    userName = data.userName,
                    profilePictureUrl = data.profilePictureUrl
                )
            )
        }

        if (result.status != HttpStatusCode.OK) {
            throw UserException(result.bodyAsText())
        }

        return result.bodyAsText().takeIf {
            it.isNotEmpty()
        }?.let {
            UserResult(
                userId = data.userId,
                email = data.email!!,
                phone = data.phone!!,
                userName = data.userName!!,
                profilePictureUrl = data.profilePictureUrl
            )
        }
    }

    override suspend fun updatePassword(token: String, data: UpdatePasswordRequest): Boolean {
        val result = httpClient.put {
            url("$baseUrl/api/updatePassword?userId=${data.userId}")
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(
                UpdatePasswordParameter(
                    password = data.password,
                    newPassword = data.newPassword
                )
            )
        }

        return result.bodyAsText() == data.userId
    }

    override suspend fun deleteUser(token: String, userId: String, password: String): Boolean {
        return httpClient.delete {
            url("$baseUrl/api/deleteUser?userId=$userId")
            contentType(ContentType.Application.Json)
            bearerAuth(token)
        }.status == HttpStatusCode.OK
    }

    override suspend fun logout(token: String): Boolean {
        return httpClient.get {
            url("$baseUrl/api/logout")
            bearerAuth(token)
        }.status == HttpStatusCode.OK
    }
}
