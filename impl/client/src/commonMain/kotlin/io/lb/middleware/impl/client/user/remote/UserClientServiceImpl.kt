package io.lb.middleware.impl.client.user.remote

import io.lb.middleware.common.data.user.remote.model.LoginRequest
import io.lb.middleware.common.data.user.remote.model.UpdatePasswordRequest
import io.lb.middleware.common.data.user.remote.UserClientService
import io.lb.middleware.common.data.user.remote.model.UserResult

/**
 * Service for making requests to the middleware server.
 */
class UserClientServiceImpl : UserClientService {
    override suspend fun login(data: LoginRequest): UserResult? {
        TODO("Not yet implemented")
    }

    override suspend fun register(data: LoginRequest): UserResult? {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(token: String, data: LoginRequest): UserResult? {
        TODO("Not yet implemented")
    }

    override suspend fun updatePassword(token: String, data: UpdatePasswordRequest): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(token: String, userId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun logout(token: String): Boolean {
        TODO("Not yet implemented")
    }
}
