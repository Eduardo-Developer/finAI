package com.edudev.finai.domain.usecase

import com.edudev.finai.domain.model.Transaction
import com.edudev.finai.domain.model.TransactionType
import com.edudev.finai.domain.repository.TransactionRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Date

class AddTransactionUseCaseTest {

    private val repository = mockk<TransactionRepository>()
    private val useCase = AddTransactionUseCase(repository)

    @Test
    fun `should call repository to insert transaction successfully`() = runTest {
        val fakeTransaction = Transaction(
            id = 0,
            amount = 100.0,
            description = "Gasolina",
            type = TransactionType.EXPENSE,
            date = Date(),
            userId = "user123",
            category = "Transporte"
        )
        val expectedId = 1L
        
        coEvery { repository.insertTransaction(any()) } returns expectedId

        val result = useCase(fakeTransaction)

        assertEquals(expectedId, result)
    }
}
