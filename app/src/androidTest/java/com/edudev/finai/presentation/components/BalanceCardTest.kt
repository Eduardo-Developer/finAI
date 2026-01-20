package com.edudev.finai.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.edudev.finai.presentation.components.dashboardScreen.BalanceCard
import com.edudev.finai.ui.theme.FinAITheme
import org.junit.Rule
import org.junit.Test
import java.text.NumberFormat
import java.util.Locale

class BalanceCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun balanceCard_shouldDisplayCorrectFormattedValues() {
        val balance = 1250.0
        val income = 2000.0
        val expense = 500.0

        val locale = Locale("pt", "BR")
        val currencyFormat = NumberFormat.getCurrencyInstance(locale)
        val expectedBalanceText = currencyFormat.format(balance)
        val expectedIncomeText = currencyFormat.format(income)
        val expectedExpenseText = currencyFormat.format(expense)

        composeTestRule.setContent {
            FinAITheme {
                BalanceCard(
                    balance = balance,
                    income = income,
                    expense = expense,
                    currencyFormat = currencyFormat
                )
            }
        }

        composeTestRule.onNodeWithText("Saldo Total").assertIsDisplayed()
        composeTestRule.onNodeWithText(expectedBalanceText).assertIsDisplayed()
        composeTestRule.onNodeWithText(expectedIncomeText).assertIsDisplayed()
        composeTestRule.onNodeWithText(expectedExpenseText).assertIsDisplayed()
    }
}
