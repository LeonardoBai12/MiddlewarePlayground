package io.middleware.signup.domain.usecases

import io.lb.middleware.common.shared.user.UserData
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.common.state.Resource
import io.lb.middleware.common.state.toCommonFlow
import io.middleware.signup.domain.error.SignUpException
import io.middleware.signup.domain.repository.SignUpRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class LoginUseCaseTest {

    private class FakeSignUpRepository : SignUpRepository {
        var userToReturn: UserData? = null
        var shouldSucceed = true
        var throwableToEmit: Throwable? = null
        var lastLoginEmail: String? = null
        var lastLoginPassword: String? = null

        override suspend fun login(email: String, password: String): CommonFlow<Resource<UserData?>> {
            lastLoginEmail = email
            lastLoginPassword = password

            return if (shouldSucceed) {
                flowOf(Resource.Success<UserData?>(userToReturn ?: UserData(
                    userName = "Test User",
                    phone = "12345678901",
                    email = email,
                    token = "auth-token"
                ))).toCommonFlow()
            } else {
                val error = throwableToEmit ?: RuntimeException("Login failed")
                flowOf(Resource.Error<UserData?>(error)).toCommonFlow()
            }
        }

        override suspend fun signUp(data: UserData, password: String): CommonFlow<Resource<UserData?>> {
            throw NotImplementedError()
        }
    }

    // Test data
    private val validEmail = "test@example.com"
    private val validPassword = "password123"

    @Test
    fun `invoke should throw when email is blank`() = runTest {
        // Given
        val useCase = LoginUseCase(FakeSignUpRepository())

        // When / Then
        assertFailsWith<SignUpException> {
            useCase(
                email = "",
                password = validPassword
            ).first()
        }.let { exception ->
            assertEquals("Email cannot be blank", exception.message)
        }
    }

    @Test
    fun `invoke should throw when email is invalid`() = runTest {
        // Given
        val useCase = LoginUseCase(FakeSignUpRepository())

        // When / Then
        assertFailsWith<SignUpException> {
            useCase(
                email = "invalid-email",
                password = validPassword
            ).first()
        }.let { exception ->
            assertEquals("Invalid email", exception.message)
        }
    }

    @Test
    fun `invoke should throw when password is blank`() = runTest {
        // Given
        val useCase = LoginUseCase(FakeSignUpRepository())

        // When / Then
        assertFailsWith<SignUpException> {
            useCase(
                email = validEmail,
                password = ""
            ).first()
        }.let { exception ->
            assertEquals("Password cannot be blank", exception.message)
        }
    }

    @Test
    fun `invoke should call repository with correct parameters when validation passes`() = runTest {
        // Given
        val fakeRepository = FakeSignUpRepository()
        val useCase = LoginUseCase(fakeRepository)

        // When
        useCase(
            email = validEmail,
            password = validPassword
        ).first()

        // Then
        assertEquals(validEmail, fakeRepository.lastLoginEmail)
        assertEquals(validPassword, fakeRepository.lastLoginPassword)
    }

    @Test
    fun `invoke should return success when repository succeeds`() = runTest {
        // Given
        val expectedUser = UserData(
            userName = "Test User",
            phone = "1234567890",
            email = validEmail,
            token = "auth-token"
        )
        val fakeRepository = FakeSignUpRepository().apply {
            shouldSucceed = true
            userToReturn = expectedUser
        }
        val useCase = LoginUseCase(fakeRepository)

        // When
        val result = useCase(
            email = validEmail,
            password = validPassword
        ).first()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(expectedUser, (result as Resource.Success).data)
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Given
        val expectedError = RuntimeException("Test error")
        val fakeRepository = FakeSignUpRepository().apply {
            shouldSucceed = false
            throwableToEmit = expectedError
        }
        val useCase = LoginUseCase(fakeRepository)

        // When
        val result = useCase(
            email = validEmail,
            password = validPassword
        ).first()

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(expectedError, (result as Resource.Error).throwable)
    }

    @Test
    fun `invoke should return user with token when login succeeds`() = runTest {
        // Given
        val expectedToken = "generated-auth-token"
        val fakeRepository = FakeSignUpRepository().apply {
            shouldSucceed = true
            userToReturn = UserData(
                userName = "Test User",
                phone = "1234567890",
                email = validEmail,
                token = expectedToken
            )
        }
        val useCase = LoginUseCase(fakeRepository)

        // When
        val result = useCase(
            email = validEmail,
            password = validPassword
        ).first()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(expectedToken, (result as Resource.Success).data?.token)
    }
}
