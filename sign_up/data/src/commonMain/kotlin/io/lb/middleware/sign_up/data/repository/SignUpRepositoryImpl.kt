package io.lb.middleware.sign_up.data.repository

import io.lb.middleware.common.remote.user.remote.model.LoginRequest
import io.lb.middleware.common.remote.user.remote.model.UserCreateRequest
import io.lb.middleware.common.shared.user.UserData
import io.lb.middleware.common.shared.user.UserException
import io.lb.middleware.common.state.Resource
import io.lb.middleware.common.state.toCommonFlow
import io.lb.middleware.sign_up.data.data_source.SignUpDataSource
import io.middleware.sign_up.domain.repository.SignUpRepository
import kotlinx.coroutines.flow.flow

class SignUpRepositoryImpl(
    private val dataSource: SignUpDataSource
) : SignUpRepository {
    override suspend fun login(email: String, password: String) = flow<Resource<UserData?>> {
        kotlin.runCatching {
            val result = dataSource.login(
                LoginRequest(
                    email = email,
                    password = password
                )
            )
            val remoteUser = dataSource.getUserById(
                token = result?.token ?: "",
                userId = result?.userId ?: ""
            )
            val userData = UserData(
                userId = remoteUser?.userId ?: "",
                token = result?.token ?: "",
                email = remoteUser?.email ?: "",
                userName = remoteUser?.userName ?: "",
                phone = remoteUser?.phone ?: "",
                profilePictureUrl = remoteUser?.profilePictureUrl ?: ""
            )
            dataSource.saveUserLocally(userData)
            emit(Resource.Success(userData))
        }.getOrElse {
            emit(Resource.Error(it))
        }
    }.toCommonFlow()

    override suspend fun signUp(data: UserData, password: String) = flow<Resource<UserData?>> {
        runCatching {
            val userId = dataSource.signUp(
                UserCreateRequest(
                    email = data.email,
                    password = password,
                    userName = data.userName,
                    phone = data.phone,
                    profilePictureUrl = data.profilePictureUrl
                )
            ) ?: throw UserException("Could not sign up user")

            val result = dataSource.login(
                LoginRequest(
                    email = data.email,
                    password = password
                )
            )

            val userData = UserData(
                userId = userId,
                token = result?.token ?: "",
                email = data.email,
                userName = data.userName,
                phone = data.phone,
                profilePictureUrl = data.profilePictureUrl
            )
            emit(Resource.Success(userData))
        }.getOrElse {
            emit(Resource.Error(it))
        }
    }.toCommonFlow()
}
