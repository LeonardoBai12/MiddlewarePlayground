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

class UpdateUserUseCaseTest {

    private class FakeUserRepository : UserRepository {
        var shouldSucceed = true
        var lastUserData: UserData? = null
        var lastPassword: String? = null
        var throwableToEmit: Throwable? = null

        override suspend fun updateUser(data: UserData, password: String): CommonFlow<Resource<UserData?>> {
            lastUserData = data
            lastPassword = password

            return if (shouldSucceed) {
                flowOf(Resource.Success<UserData?>(data.copy(userId = "123", token = "token123"))).toCommonFlow()
            } else {
                val error = throwableToEmit ?: RuntimeException("Update failed")
                flowOf(Resource.Error<UserData?>(error)).toCommonFlow()
            }
        }

        // Other required overrides with empty implementations
        override suspend fun getCurrentUser(): UserData? = null
        override suspend fun updatePassword(password: String, newPassword: String): CommonFlow<Resource<Unit>> {
            throw NotImplementedError()
        }
        override suspend fun deleteUser(password: String): CommonFlow<Resource<Unit>> {
            throw NotImplementedError()
        }
        override suspend fun logout(): CommonFlow<Resource<Unit>> {
            throw NotImplementedError()
        }
    }

    // Test data
    private val validUserData = UserData(
        userName = "John Doe",
        phone = "12345678901",
        email = "john@example.com",
        profilePictureUrl = "http://example.com/photo.jpg"
    )
    private val validPassword = "Password123!"

    @Test
    fun `invoke should throw when userName is blank`() = runTest {
        // Given
        val useCase = UpdateUserUseCase(FakeUserRepository())

        // When / Then
        assertFailsWith<UserException> {
            useCase(
                userName = "",
                phone = validUserData.phone,
                email = validUserData.email,
                profilePictureUrl = validUserData.profilePictureUrl,
                password = validPassword
            ).single()
        }.let { exception ->
            assertEquals("User name is required.", exception.message)
        }
    }

    @Test
    fun `invoke should throw when phone is blank`() = runTest {
        // Given
        val useCase = UpdateUserUseCase(FakeUserRepository())

        // When / Then
        assertFailsWith<UserException> {
            useCase(
                userName = validUserData.userName,
                phone = "",
                email = validUserData.email,
                profilePictureUrl = validUserData.profilePictureUrl,
                password = validPassword
            ).single()
        }.let { exception ->
            assertEquals("Phone is required.", exception.message)
        }
    }

    @Test
    fun `invoke should throw when email is blank`() = runTest {
        // Given
        val useCase = UpdateUserUseCase(FakeUserRepository())

        // When / Then
        assertFailsWith<UserException> {
            useCase(
                userName = validUserData.userName,
                phone = validUserData.phone,
                email = "",
                profilePictureUrl = validUserData.profilePictureUrl,
                password = validPassword
            ).single()
        }.let { exception ->
            assertEquals("Email is required.", exception.message)
        }
    }

    @Test
    fun `invoke should throw when email is invalid`() = runTest {
        // Given
        val useCase = UpdateUserUseCase(FakeUserRepository())

        // When / Then
        assertFailsWith<UserException> {
            useCase(
                userName = validUserData.userName,
                phone = validUserData.phone,
                email = "invalid-email",
                profilePictureUrl = validUserData.profilePictureUrl,
                password = validPassword
            ).single()
        }.let { exception ->
            assertEquals("Invalid email.", exception.message)
        }
    }

    @Test
    fun `invoke should throw when phone is invalid`() = runTest {
        // Given
        val useCase = UpdateUserUseCase(FakeUserRepository())

        // When / Then
        assertFailsWith<UserException> {
            useCase(
                userName = validUserData.userName,
                phone = "123", // Invalid phone
                email = validUserData.email,
                profilePictureUrl = validUserData.profilePictureUrl,
                password = validPassword
            ).single()
        }.let { exception ->
            assertEquals("Invalid phone", exception.message)
        }
    }

    @Test
    fun `invoke should throw when password is blank`() = runTest {
        // Given
        val useCase = UpdateUserUseCase(FakeUserRepository())

        // When / Then
        assertFailsWith<UserException> {
            useCase(
                userName = validUserData.userName,
                phone = validUserData.phone,
                email = validUserData.email,
                profilePictureUrl = validUserData.profilePictureUrl,
                password = ""
            ).single()
        }.let { exception ->
            assertEquals("Password is required.", exception.message)
        }
    }

    @Test
    fun `invoke should call repository with correct parameters when validation passes`() = runTest {
        // Given
        val fakeRepository = FakeUserRepository()
        val useCase = UpdateUserUseCase(fakeRepository)

        // When
        useCase(
            userName = validUserData.userName,
            phone = validUserData.phone,
            email = validUserData.email,
            profilePictureUrl = validUserData.profilePictureUrl,
            password = validPassword
        ).single()

        // Then
        assertEquals(validUserData.copy(profilePictureUrl = validUserData.profilePictureUrl), fakeRepository.lastUserData)
        assertEquals(validPassword, fakeRepository.lastPassword)
    }

    @Test
    fun `invoke should return success with user data when repository succeeds`() = runTest {
        // Given
        val fakeRepository = FakeUserRepository().apply { shouldSucceed = true }
        val useCase = UpdateUserUseCase(fakeRepository)

        // When
        val result = useCase(
            userName = validUserData.userName,
            phone = validUserData.phone,
            email = validUserData.email,
            profilePictureUrl = validUserData.profilePictureUrl,
            password = validPassword
        ).single()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("123", result.data?.userId)
        assertEquals("token123", result.data?.token)
        assertEquals(validUserData.userName, result.data?.userName)
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val expectedError = RuntimeException("Test error")
        val fakeRepository = FakeUserRepository().apply {
            shouldSucceed = false
            throwableToEmit = expectedError
        }
        val useCase = UpdateUserUseCase(fakeRepository)

        // When
        val result = useCase(
            userName = validUserData.userName,
            phone = validUserData.phone,
            email = validUserData.email,
            profilePictureUrl = validUserData.profilePictureUrl,
            password = validPassword
        ).single()

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(expectedError, (result as Resource.Error).throwable)
    }

    @Test
    fun `invoke should work when profilePictureUrl is null`() = runTest {
        // Given
        val fakeRepository = FakeUserRepository()
        val useCase = UpdateUserUseCase(fakeRepository)

        // When
        useCase(
            userName = validUserData.userName,
            phone = validUserData.phone,
            email = validUserData.email,
            profilePictureUrl = null,
            password = validPassword
        ).single()

        // Then
        assertEquals(validUserData.copy(profilePictureUrl = null), fakeRepository.lastUserData)
    }
}
