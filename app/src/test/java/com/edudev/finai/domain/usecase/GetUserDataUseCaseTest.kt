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

            // GIVEN - Dado que o repositório retornará um Flow com um usuário falso
            whenever(mockAuthRepository.getUserData(fakeUserId)).thenReturn(fakeUserFlow)

            // Act (Agir)
            // WHEN - Quando o use case é invocado com o ID do usuário
            val resultFlow = getUserDataUseCase.invoke(fakeUserId)

            // Assert (Afirmar)
            // THEN - Então o primeiro valor emitido pelo Flow resultante deve ser o usuário falso
            assertEquals(fakeUser, resultFlow.first())

            // E o método getUserData do repositório deve ter sido chamado exatamente uma vez
            verify(mockAuthRepository).getUserData(fakeUserId)
        }
    }
}
