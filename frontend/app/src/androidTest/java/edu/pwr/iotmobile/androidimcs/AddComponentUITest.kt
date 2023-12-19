package edu.pwr.iotmobile.androidimcs

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent.AddComponentNavigation
import edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent.AddComponentScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddComponentUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun before() {
        composeTestRule.setContent {
            AddComponentScreen(navigation = AddComponentNavigation.empty())
        }
    }

    @Test
    fun defaultContentTest() {
        composeTestRule.onNodeWithTag("navWrapper").assertExists()
        composeTestRule.onNodeWithTag("navWrapper").assertIsDisplayed()
        composeTestRule.onNodeWithTag("ChooseComponentContent").assertExists()
        composeTestRule.onNodeWithTag("ChooseComponentContent").assertIsDisplayed()

        composeTestRule.onAllNodes(hasTestTag("componentChoiceItem")).assertCountEquals(12)

        composeTestRule.onAllNodes(hasTestTag("componentChoiceItem")).apply {
            fetchSemanticsNodes().forEachIndexed { i, _ ->
                get(i).assertHasClickAction()
                get(i).assertHeightIsAtLeast(120.dp)
            }
        }
    }
}