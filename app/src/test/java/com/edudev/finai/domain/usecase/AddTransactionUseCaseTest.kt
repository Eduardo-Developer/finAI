
package com.edudev.finai.domain.usecase

import com.edudev.finai.domain.model.Transaction
import com.edudev.finai.domain.model.TransactionType
import com.edudev.finai.domain.repository.TransactionRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import java.util.Date

@ExtendWith(MockitoExtension::class)
class AddTransactionUseCaseTest {

    @Mock
    private lateinit var mockTransactionRepository: TransactionRepository

    private lateinit var addTransactionUseCase: AddTransactionUseCase

    @BeforeEach
    fun setUp() {
        addTransactionUseCase = AddTransactionUseCase(mockTransactionRepository)
    }

    @Test
    fun `invoke should call insertTransaction on repository`() {
        runBlocking {
            // Arrange (Arranjar)
            // GIVEN - Uma transação falsa é criada
            val fakeTransaction = Transaction(
                id = 1L,
                userId = "testUser",
                amount = 100.0,
                category = "Salário",
                description = "Pagamento do mês",
                type = TransactionType.INCOME,
                date = Date()
            )

            // Act (Agir)
            // WHEN - O use case é invocado com a transação falsa
            addTransactionUseCase.invoke(fakeTransaction)

            // Assert (Afirmar)
            // THEN - O método insertTransaction do repositório deve ser chamado com a transação
            verify(mockTransactionRepository).insertTransaction(fakeTransaction)
        }
    }
}
