package com.folkmanis.laiks.ui.screens.appliance_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.hasImeAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.text.input.ImeAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NumberInputTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun text_selected_and_replaced() {

        var testNumber: Long? = 5L
        composeTestRule.setContent {
            MaterialTheme {
                NumberInput(
                    value = testNumber,
                    onValueChange = { testNumber = it }
                )
            }
        }

        composeTestRule
            .onNode(hasSetTextAction())
            .performClick()
            .performTextInput("3")

        assert(testNumber == 3L)
    }

    @Test
    fun empty_input_to_zero() {

        composeTestRule.setContent {
            var value by remember {
                mutableStateOf<Long?>(5L)
            }
            MaterialTheme {
                Column {
                    NumberInput(
                        value = value,
                        onValueChange = {
                            value = it
                        }
                    )
                }
            }
        }

        composeTestRule
            .onNode(hasSetTextAction())
            .performTextClearance()

        composeTestRule.onNodeWithText("").assertExists()

        composeTestRule
            .onNode(hasImeAction(ImeAction.Next))
            .performImeAction()
        composeTestRule.onNodeWithText("0").assertExists()

        composeTestRule.onRoot().printToLog(TAG)

    }

    companion object {
        private const val TAG = "NumberInputTest"
    }
}