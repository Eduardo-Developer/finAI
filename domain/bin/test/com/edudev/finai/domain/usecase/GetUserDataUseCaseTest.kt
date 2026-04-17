package com.edudev.finai.domain.usecase

import com.edudev.finai.domain.model.User
import com.edudev.finai.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class GetUserDataUseCaseTest {

    @Mock
    private lateinit var mockAuthRepository: AuthRepository

    private lateinit var getUserDataUseCase: GetUserDataUseCase

    @BeforeEach
    fun setUp() {
        getUserDataUseCase = GetUserDataUseCase(mockAuthRepository)
    }

    @Test
    fun `invoke should return user flow from repository`() {
        runBlocking {
            val fakeUserId = "123"
            val fakeUser = User(uid = fakeUserId, fullName = "Eduardo", email = "test@test.com")
            val fakeUserFlow = flowOf(fakeUser)

            whenever(mockAuthRepository.getUserData(fakeUserId)).thenReturn(fakeUserFlow)

            val resultFlow = getUserDataUseCase.invoke(fakeUserId)

            assertEquals(fakeUser, resultFlow.first())

            verify(mockAuthRepository).getUserData(fakeUserId)
        }
    }
}
