package io.middleware.user.domain.usecases

import io.lb.middleware.common.shared.user.UserData
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource
import io.lb.middleware.common.state.toCommonFlow
import io.middleware.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DeleteUserUseCaseTest {
    private class FakeUserRepository : UserRepository {
        var shouldSucceed = true
        var lastDeletedPassword: String? = null
        var throwableToEmit: Throwable? = null

        override suspend fun getCurrentUser(): UserData? = null

        override suspend fun updateUser(data: UserData, password: String): CommonFlow<Resource<UserData?>> {
            throw NotImplementedError()
        }

        override suspend fun updatePassword(password: String, newPassword: String): CommonFlow<Resource<Unit>> {
            throw NotImplementedError()
        }

        override suspend fun deleteUser(password: String): CommonFlow<Resource<Unit>> {
            lastDeletedPassword = password

            return if (shouldSucceed) {
                flowOf(Resource.Success(Unit)).toCommonFlow()
            } else {
                val error = throwableToEmit ?: RuntimeException("Delete failed")
                flowOf(Resource.Error<Unit>(error)).toCommonFlow()
            }
        }

        override suspend fun logout(): CommonFlow<Resource<Unit>> {
            throw NotImplementedError()
        }
    }

    @Test
    fun `invoke should call repository deleteUser with correct password`() = runTest {
        // Given
        val fakeRepository = FakeUserRepository()
        val useCase = DeleteUserUseCase(fakeRepository)
        val testPassword = "testPassword123"

        // When
        useCase(testPassword).single()

        // Then
        assertEquals(testPassword, fakeRepository.lastDeletedPassword)
    }

    @Test
    fun `invoke should return success when repository succeeds`() = runTest {
        // Given
        val fakeRepository = FakeUserRepository().apply { shouldSucceed = true }
        val useCase = DeleteUserUseCase(fakeRepository)

        // When
        var result: Resource<Unit>? = null
        useCase("password").collect { result = it }

        // Then
        assertTrue(result is Resource.Success)
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val expectedError = RuntimeException("Test error")
        val fakeRepository = FakeUserRepository().apply {
            shouldSucceed = false
            throwableToEmit = expectedError
        }
        val useCase = DeleteUserUseCase(fakeRepository)

        // When
        var result: Resource<Unit>? = null
        useCase("password").collect { result = it }

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(expectedError, (result as Resource.Error).throwable)
    }

    @Test
    fun `invoke should propagate error when repository throws`() = runTest {
        // Given
        val expectedError = RuntimeException("Test error")
        val fakeRepository = object : UserRepository {
            override suspend fun deleteUser(password: String): CommonFlow<Resource<Unit>> {
                throw expectedError
            }
            // Other required overrides with empty implementations
            override suspend fun getCurrentUser(): UserData? = null
            override suspend fun updateUser(data: UserData, password: String): CommonFlow<Resource<UserData?>> {
                throw NotImplementedError()
            }
            override suspend fun updatePassword(password: String, newPassword: String): CommonFlow<Resource<Unit>> {
                throw NotImplementedError()
            }
            override suspend fun logout(): CommonFlow<Resource<Unit>> {
                throw NotImplementedError()
            }
        }
        val useCase = DeleteUserUseCase(fakeRepository)

        // When / Then
        try {
            useCase("password").single()
            assertTrue(false, "Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals(expectedError, e)
        }
    }
}
