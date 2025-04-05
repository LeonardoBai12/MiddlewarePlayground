package io.lb.middleware.signup.data.repository

import io.lb.middleware.common.remote.user.remote.model.UpdatePasswordRequest
import io.lb.middleware.common.remote.user.remote.model.UserUpdateRequest
import io.lb.middleware.common.shared.user.UserData
import io.lb.middleware.common.shared.user.UserException
import io.lb.middleware.common.state.Resource
import io.lb.middleware.common.state.toCommonFlow
import io.lb.middleware.signup.data.datasource.UserDataSource
import io.middleware.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(
    private val dataSource: UserDataSource
) : UserRepository {
    override suspend fun getCurrentUser(): UserData? {
        return dataSource.getCurrentUser()
    }

    override suspend fun updateUser(data: UserData, password: String) = flow<Resource<UserData?>> {
        runCatching {
            val currentUser = dataSource.getCurrentUser() ?: throw UserException("User not found")
            val result = dataSource.updateRemoteUser(
                token = currentUser.token ?: "",
                data = UserUpdateRequest(
                    userId = data.userId ?: currentUser.userId ?: "",
                    userName = data.userName,
                    password = password,
                    phone = data.phone,
                    email = data.email,
                    profilePictureUrl = data.profilePictureUrl,
                )
            )
            dataSource.updateUserLocally(data.copy(token = currentUser.token))

            emit(
                result?.let {
                    Resource.Success(data)
                } ?: Resource.Error(UserException("Failed to update user"))
            )
        }.getOrElse {
            emit(Resource.Error(it))
        }
    }.toCommonFlow()

    override suspend fun updatePassword(
        password: String,
        newPassword: String
    ) = flow<Resource<Unit>> {
        runCatching {
            val currentUser = dataSource.getCurrentUser() ?: throw UserException("User not found")
            val result = dataSource.updateRemotePassword(
                token = currentUser.token ?: "",
                data = UpdatePasswordRequest(
                    userId = currentUser.userId ?: "",
                    password = password,
                    newPassword = newPassword
                )
            )

            if (result) {
                emit(Resource.Success(Unit))
            } else {
                throw UserException("Failed to update password")
            }
        }.getOrElse {
            emit(Resource.Error(it))
        }
    }.toCommonFlow()

    override suspend fun deleteUser(password: String) = flow<Resource<Unit>> {
        runCatching {
            val currentUser = dataSource.getCurrentUser() ?: throw UserException("User not found")
            val result = dataSource.deleteRemoteUser(
                token = currentUser.token ?: "",
                userId = currentUser.userId ?: "",
                password = password
            )
            dataSource.deleteUserLocally(currentUser.userId ?: "")

            if (result) {
                dataSource.deleteAllRoutes()
                dataSource.deleteAllApis()
                dataSource.deleteUserLocally(currentUser.userId ?: "")
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error(UserException("Failed to delete user")))
            }
        }.getOrElse {
            emit(Resource.Error(it))
        }
    }.toCommonFlow()

    override suspend fun logout() = flow<Resource<Unit>> {
        runCatching {
            val currentUser = dataSource.getCurrentUser() ?: throw UserException("User not found")
            dataSource.logout(currentUser.token ?: "")
            dataSource.deleteAllRoutes()
            dataSource.deleteAllApis()
            dataSource.deleteUserLocally(currentUser.userId ?: "")
            emit(Resource.Success(Unit))
        }.getOrElse {
            emit(Resource.Error(it))
        }
    }.toCommonFlow()
}
