package io.middleware.splash.domain.usecases

import io.lb.middleware.common.shared.user.UserData
import io.middleware.splash.domain.repository.SplashRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GetCurrentUserOnInitUseCaseTest {
    private class FakeSplashRepository : SplashRepository {
        var userToReturn: UserData? = null
        var exceptionToThrow: Throwable? = null

        override suspend fun getCurrentUser(): UserData? {
            exceptionToThrow?.let { throw it }
            return userToReturn
        }
    }

    @Test
    fun `invoke should return user when repository returns user`() = runTest {
        // Given
        val expectedUser = UserData(
            userName = "testUser",
            phone = "1234567890",
            email = "test@example.com"
        )
        val fakeRepository = FakeSplashRepository().apply {
            userToReturn = expectedUser
        }
        val useCase = GetCurrentUserOnInitUseCase(fakeRepository)

        // When
        val result = useCase()

        // Then
        assertEquals(expectedUser, result)
    }

    @Test
    fun `invoke should return null when repository returns null`() = runTest {
        // Given
        val fakeRepository = FakeSplashRepository().apply {
            userToReturn = null
        }
        val useCase = GetCurrentUserOnInitUseCase(fakeRepository)

        // When
        val result = useCase()

        // Then
        assertNull(result)
    }

    @Test
    fun `invoke should throw when repository throws`() = runTest {
        // Given
        val expectedException = RuntimeException("Test exception")
        val fakeRepository = FakeSplashRepository().apply {
            exceptionToThrow = expectedException
        }
        val useCase = GetCurrentUserOnInitUseCase(fakeRepository)

        // When / Then
        try {
            useCase()
            assertTrue(false, "Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals(expectedException, e)
        }
    }

    @Test
    fun `invoke should return user with all fields when available`() = runTest {
        // Given
        val expectedUser = UserData(
            userId = "user123",
            userName = "testUser",
            phone = "1234567890",
            email = "test@example.com",
            profilePictureUrl = "http://example.com/avatar.jpg",
            token = "authToken123"
        )
        val fakeRepository = FakeSplashRepository().apply {
            userToReturn = expectedUser
        }
        val useCase = GetCurrentUserOnInitUseCase(fakeRepository)

        // When
        val result = useCase()

        // Then
        assertEquals(expectedUser.userId, result?.userId)
        assertEquals(expectedUser.userName, result?.userName)
        assertEquals(expectedUser.phone, result?.phone)
        assertEquals(expectedUser.email, result?.email)
        assertEquals(expectedUser.profilePictureUrl, result?.profilePictureUrl)
        assertEquals(expectedUser.token, result?.token)
    }
}
