package io.middleware.user.domain.usecases

import io.lb.middleware.common.shared.user.UserData
import io.lb.middleware.common.shared.user.UserException
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource
import io.lb.middleware.common.state.toCommonFlow
import io.middleware.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class UpdatePasswordUseCaseTest {

    private class FakeUserRepository : UserRepository {
        var shouldSucceed = true
        var lastPassword: String? = null
        var lastNewPassword: String? = null
        var throwableToEmit: Throwable? = null

        override suspend fun updatePassword(password: String, newPassword: String): CommonFlow<Resource<Unit>> {
            lastPassword = password
            lastNewPassword = newPassword

            return if (shouldSucceed) {
                flowOf(Resource.Success(Unit)).toCommonFlow()
            } else {
                val error = throwableToEmit ?: RuntimeException("Update failed")
                flowOf(Resource.Error<Unit>(error)).toCommonFlow()
            }
        }

        // Other required overrides with empty implementations
        override suspend fun getCurrentUser(): UserData? = null
        override suspend fun updateUser(data: UserData, password: String): CommonFlow<Resource<UserData?>> {
            throw NotImplementedError()
        }
        override suspend fun deleteUser(password: String): CommonFlow<Resource<Unit>> {
            throw NotImplementedError()
        }
        override suspend fun logout(): CommonFlow<Resource<Unit>> {
            throw NotImplementedError()
        }
    }

    @Test
    fun `invoke should throw when password is blank`() = runTest {
        // Given
        val useCase = UpdatePasswordUseCase(FakeUserRepository())

        // When / Then
        assertFailsWith<UserException> {
            useCase(
                password = "",
                repeatedPassword = "",
                newPassword = "NewPass123!"
            ).single()
        }.let { exception ->
            assertEquals("Password cannot be blank", exception.message)
        }
    }

    @Test
    fun `invoke should throw when passwords don't match`() = runTest {
        // Given
        val useCase = UpdatePasswordUseCase(FakeUserRepository())

        // When / Then
        assertFailsWith<UserException> {
            useCase(
                password = "OldPass123!",
                repeatedPassword = "DifferentPass123!",
                newPassword = "NewPass123!"
            ).single()
        }.let { exception ->
            assertEquals("Passwords do not match", exception.message)
        }
    }

    @Test
    fun `invoke should throw when new password is not strong`() = runTest {
        // Given
        val useCase = UpdatePasswordUseCase(FakeUserRepository())

        // When / Then
        assertFailsWith<UserException> {
            useCase(
                password = "OldPass123!",
                repeatedPassword = "OldPass123!",
                newPassword = "weak"
            ).single()
        }.let { exception ->
            assertEquals("Password is not strong enough", exception.message)
        }
    }

    @Test
    fun `invoke should call repository with correct parameters when validation passes`() = runTest {
        // Given
        val fakeRepository = FakeUserRepository()
        val useCase = UpdatePasswordUseCase(fakeRepository)
        val oldPassword = "OldPass123!"
        val newPassword = "NewPass123!"

        // When
        useCase(
            password = oldPassword,
            repeatedPassword = oldPassword,
            newPassword = newPassword
        ).single()

        // Then
        assertEquals(oldPassword, fakeRepository.lastPassword)
        assertEquals(newPassword, fakeRepository.lastNewPassword)
    }

    @Test
    fun `invoke should return success when repository succeeds`() = runTest {
        // Given
        val fakeRepository = FakeUserRepository().apply { shouldSucceed = true }
        val useCase = UpdatePasswordUseCase(fakeRepository)

        // When
        val result = useCase(
            password = "OldPass123!",
            repeatedPassword = "OldPass123!",
            newPassword = "NewPass123!"
        ).single()

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
        val useCase = UpdatePasswordUseCase(fakeRepository)

        // When
        val result = useCase(
            password = "OldPass123!",
            repeatedPassword = "OldPass123!",
            newPassword = "NewPass123!"
        ).single()

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(expectedError, (result as Resource.Error).throwable)
    }

    @Test
    fun `invoke should propagate error when repository throws`() = runTest {
        // Given
        val expectedError = RuntimeException("Test error")
        val fakeRepository = object : UserRepository {
            override suspend fun updatePassword(password: String, newPassword: String): CommonFlow<Resource<Unit>> {
                throw expectedError
            }
            // Other required overrides
            override suspend fun getCurrentUser(): UserData? = null
            override suspend fun updateUser(data: UserData, password: String): CommonFlow<Resource<UserData?>> {
                throw NotImplementedError()
            }
            override suspend fun deleteUser(password: String): CommonFlow<Resource<Unit>> {
                throw NotImplementedError()
            }
            override suspend fun logout(): CommonFlow<Resource<Unit>> {
                throw NotImplementedError()
            }
        }
        val useCase = UpdatePasswordUseCase(fakeRepository)

        // When / Then
        try {
            useCase(
                password = "OldPass123!",
                repeatedPassword = "OldPass123!",
                newPassword = "NewPass123!"
            ).single()
            assertTrue(false, "Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals(expectedError, e)
        }
    }
}
