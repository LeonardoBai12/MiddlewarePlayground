package io.lb.middleware.sign_up.data.repository

import io.lb.middleware.common.remote.user.remote.model.UpdatePasswordRequest
import io.lb.middleware.common.remote.user.remote.model.UserUpdateRequest
import io.lb.middleware.common.shared.user.UserData
import io.lb.middleware.common.shared.user.UserException
import io.lb.middleware.common.state.Resource
import io.lb.middleware.common.state.toCommonFlow
import io.lb.middleware.sign_up.data.data_source.UserDataSource
import io.middleware.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(
    private val dataSource: UserDataSource
) : UserRepository {
    override suspend fun getCurrentUser(): UserData? {
        return dataSource.getCurrentUser()
    }

    override suspend fun updateUser(data: UserData, password: String) = flow<Resource<UserData?>> {
        val currentUser = dataSource.getCurrentUser() ?: run {
            emit(Resource.Error(UserException("User not found")))
            return@flow
        }

        val result = kotlin.runCatching {
            dataSource.updateRemoteUser(
                token = currentUser.token ?: "",
                data = UserUpdateRequest(
                    userId = data.userId,
                    userName = data.userName,
                    password = password,
                    phone = data.phone,
                    email = data.email,
                    profilePictureUrl = data.profilePictureUrl,
                )
            )
        }.getOrElse {
            emit(
                Resource.Error(UserException(it.message ?: "Failed to update user"))
            )
            return@flow
        }

        emit(
            result?.let {
                Resource.Success(data)
            } ?: Resource.Error(UserException("Failed to update user"))
        )
    }.toCommonFlow()

    override suspend fun updatePassword(
        password: String,
        newPassword: String
    ) = flow {
        val currentUser = dataSource.getCurrentUser() ?: run {
            emit(Resource.Error(UserException("User not found")))
            return@flow
        }

        val result = kotlin.runCatching {
            dataSource.updateRemotePassword(
                token = currentUser.token ?: "",
                data = UpdatePasswordRequest(
                    userId = currentUser.userId,
                    password = password,
                    newPassword = newPassword
                )
            )
        }.getOrElse {
            emit(
                Resource.Error(UserException(it.message ?: "Failed to update password"))
            )
            return@flow
        }

        emit(
            if (result) {
                Resource.Success(Unit)
            } else {
                Resource.Error(UserException("Failed to update password"))
            }
        )
    }.toCommonFlow()

    override suspend fun deleteUser(password: String) = flow {
        val currentUser = dataSource.getCurrentUser() ?: run {
            emit(Resource.Error(UserException("User not found")))
            return@flow
        }

        val result = kotlin.runCatching {
            dataSource.deleteUserLocally(currentUser.userId)
            dataSource.deleteRemoteUser(
                token = currentUser.token ?: "",
                userId = currentUser.userId,
                password = password
            )
        }.getOrElse {
            emit(
                Resource.Error(UserException(it.message ?: "Failed to delete user"))
            )
            return@flow
        }

        if (result) {
            dataSource.deleteAllRoutes()
            dataSource.deleteAllApis()
            dataSource.deleteUserLocally(currentUser.userId)
            emit(Resource.Success(Unit))
        } else {
            emit(Resource.Error(UserException("Failed to delete user")))
        }
    }.toCommonFlow()

    override suspend fun logout() = flow {
        val currentUser = dataSource.getCurrentUser() ?: run {
            emit(Resource.Error(UserException("User not found")))
            return@flow
        }

        val result = kotlin.runCatching {
            dataSource.logout(currentUser.token ?: "")
        }.getOrElse {
            emit(
                Resource.Error(UserException(it.message ?: "Failed to logout"))
            )
            return@flow
        }

        if (result) {
            dataSource.deleteAllRoutes()
            dataSource.deleteAllApis()
            dataSource.deleteUserLocally(currentUser.userId)
            emit(Resource.Success(Unit))
        } else {
            emit(Resource.Error(UserException("Failed to logout")))
        }
    }.toCommonFlow()
}
