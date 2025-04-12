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
import kotlin.uuid.ExperimentalUuidApi

@ExperimentalUuidApi
class SignUpUseCaseTest {

    private class FakeSignUpRepository : SignUpRepository {
        var userToReturn: UserData? = null
        var shouldSucceed = true
        var throwableToEmit: Throwable? = null
        var lastSignUpData: UserData? = null
        var lastPassword: String? = null

        override suspend fun login(email: String, password: String): CommonFlow<Resource<UserData?>> {
            throw NotImplementedError()
        }

        override suspend fun signUp(data: UserData, password: String): CommonFlow<Resource<UserData?>> {
            lastSignUpData = data
            lastPassword = password

            return if (shouldSucceed) {
                flowOf(Resource.Success<UserData?>(userToReturn ?: data.copy(userId = "generated-id")))
                    .toCommonFlow()
            } else {
                val error = throwableToEmit ?: RuntimeException("Sign up failed")
                flowOf(Resource.Error<UserData?>(error)).toCommonFlow()
            }
        }
    }

    // Test data
    private val validUserData = UserData(
        userName = "John Doe",
        phone = "12345678901",
        email = "john@example.com",
        profilePictureUrl = "http://example.com/photo.jpg"
    )
    private val validPassword = "StrongPass123!"
    private val validRepeatedPassword = "StrongPass123!"

    @Test
    fun `invoke should throw when email is blank`() = runTest {
        // Given
        val useCase = SignUpUseCase(FakeSignUpRepository())

        // When / Then
        assertFailsWith<SignUpException> {
            useCase(
                userName = validUserData.userName,
                phone = validUserData.phone,
                email = "",
                profilePictureUrl = validUserData.profilePictureUrl,
                password = validPassword,
                repeatedPassword = validRepeatedPassword
            )
        }.let { exception ->
            assertEquals("Email cannot be blank", exception.message)
        }
    }

    @Test
    fun `invoke should throw when email is invalid`() = runTest {
        // Given
        val useCase = SignUpUseCase(FakeSignUpRepository())

        // When / Then
        assertFailsWith<SignUpException> {
            useCase(
                userName = validUserData.userName,
                phone = validUserData.phone,
                email = "invalid-email",
                profilePictureUrl = validUserData.profilePictureUrl,
                password = validPassword,
                repeatedPassword = validRepeatedPassword
            )
        }.let { exception ->
            assertEquals("Invalid email", exception.message)
        }
    }

    @Test
    fun `invoke should throw when password is blank`() = runTest {
        // Given
        val useCase = SignUpUseCase(FakeSignUpRepository())

        // When / Then
        assertFailsWith<SignUpException> {
            useCase(
                userName = validUserData.userName,
                phone = validUserData.phone,
                email = validUserData.email,
                profilePictureUrl = validUserData.profilePictureUrl,
                password = "",
                repeatedPassword = ""
            )
        }.let { exception ->
            assertEquals("Password cannot be blank", exception.message)
        }
    }

    @Test
    fun `invoke should throw when password is not strong`() = runTest {
        // Given
        val useCase = SignUpUseCase(FakeSignUpRepository())

        // When / Then
        assertFailsWith<SignUpException> {
            useCase(
                userName = validUserData.userName,
                phone = validUserData.phone,
                email = validUserData.email,
                profilePictureUrl = validUserData.profilePictureUrl,
                password = "weak",
                repeatedPassword = "weak"
            )
        }.let { exception ->
            assertEquals("Password is not strong enough", exception.message)
        }
    }

    @Test
    fun `invoke should throw when passwords don't match`() = runTest {
        // Given
        val useCase = SignUpUseCase(FakeSignUpRepository())

        // When / Then
        assertFailsWith<SignUpException> {
            useCase(
                userName = validUserData.userName,
                phone = validUserData.phone,
                email = validUserData.email,
                profilePictureUrl = validUserData.profilePictureUrl,
                password = validPassword,
                repeatedPassword = "different"
            )
        }.let { exception ->
            assertEquals("Passwords do not match", exception.message)
        }
    }

    @Test
    fun `invoke should throw when name is blank`() = runTest {
        // Given
        val useCase = SignUpUseCase(FakeSignUpRepository())

        // When / Then
        assertFailsWith<SignUpException> {
            useCase(
                userName = "",
                phone = validUserData.phone,
                email = validUserData.email,
                profilePictureUrl = validUserData.profilePictureUrl,
                password = validPassword,
                repeatedPassword = validRepeatedPassword
            )
        }.let { exception ->
            assertEquals("Name cannot be blank", exception.message)
        }
    }

    @Test
    fun `invoke should throw when phone is blank`() = runTest {
        // Given
        val useCase = SignUpUseCase(FakeSignUpRepository())

        // When / Then
        assertFailsWith<SignUpException> {
            useCase(
                userName = validUserData.userName,
                phone = "",
                email = validUserData.email,
                profilePictureUrl = validUserData.profilePictureUrl,
                password = validPassword,
                repeatedPassword = validRepeatedPassword
            )
        }.let { exception ->
            assertEquals("Phone cannot be blank", exception.message)
        }
    }

    @Test
    fun `invoke should throw when phone is invalid`() = runTest {
        // Given
        val useCase = SignUpUseCase(FakeSignUpRepository())

        // When / Then
        assertFailsWith<SignUpException> {
            useCase(
                userName = validUserData.userName,
                phone = "123",
                email = validUserData.email,
                profilePictureUrl = validUserData.profilePictureUrl,
                password = validPassword,
                repeatedPassword = validRepeatedPassword
            )
        }.let { exception ->
            assertEquals("Invalid phone", exception.message)
        }
    }

    @Test
    fun `invoke should call repository with correct parameters when validation passes`() = runTest {
        // Given
        val fakeRepository = FakeSignUpRepository()
        val useCase = SignUpUseCase(fakeRepository)

        // When
        useCase(
            userName = validUserData.userName,
            phone = validUserData.phone,
            email = validUserData.email,
            profilePictureUrl = validUserData.profilePictureUrl,
            password = validPassword,
            repeatedPassword = validRepeatedPassword
        ).first()

        // Then
        assertEquals(validUserData.copy(profilePictureUrl = validUserData.profilePictureUrl),
            fakeRepository.lastSignUpData)
        assertEquals(validPassword, fakeRepository.lastPassword)
    }

    @Test
    fun `invoke should return success when repository succeeds`() = runTest {
        // Given
        val expectedUser = validUserData.copy(userId = "generated-id")
        val fakeRepository = FakeSignUpRepository().apply {
            shouldSucceed = true
            userToReturn = expectedUser
        }
        val useCase = SignUpUseCase(fakeRepository)

        // When
        val result = useCase(
            userName = validUserData.userName,
            phone = validUserData.phone,
            email = validUserData.email,
            profilePictureUrl = validUserData.profilePictureUrl,
            password = validPassword,
            repeatedPassword = validRepeatedPassword
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
        val useCase = SignUpUseCase(fakeRepository)

        // When
        val result = useCase(
            userName = validUserData.userName,
            phone = validUserData.phone,
            email = validUserData.email,
            profilePictureUrl = validUserData.profilePictureUrl,
            password = validPassword,
            repeatedPassword = validRepeatedPassword
        ).first()

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(expectedError, (result as Resource.Error).throwable)
    }

    @Test
    fun `invoke should work when profilePictureUrl is null`() = runTest {
        // Given
        val fakeRepository = FakeSignUpRepository()
        val useCase = SignUpUseCase(fakeRepository)

        // When
        useCase(
            userName = validUserData.userName,
            phone = validUserData.phone,
            email = validUserData.email,
            profilePictureUrl = null,
            password = validPassword,
            repeatedPassword = validRepeatedPassword
        ).first()

        // Then
        assertEquals(validUserData.copy(profilePictureUrl = null), fakeRepository.lastSignUpData)
    }
}
