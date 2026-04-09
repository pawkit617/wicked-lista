package com.example.wickedlista.ui.screens

import android.content.Context
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.wickedlista.HiltTestActivity
import com.example.wickedlista.R
import com.example.wickedlista.WickedListaApp
import com.example.wickedlista.database.WickedListaDatabase
import com.example.wickedlista.ui.theme.WickedListaTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@OptIn(ExperimentalTestApi::class)
class HomeScreenTest {
    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule
    var composeTestRuleActivity = createAndroidComposeRule<HiltTestActivity>()

    private lateinit var baseContext: Context

    @Inject
    lateinit var database : WickedListaDatabase

    private val delayForUiVisibility = 1000L
    @Before
    fun setUp() {
        composeTestRuleActivity.setContent {
            WickedListaTheme {
                WickedListaApp()
            }
        }
        baseContext = composeTestRuleActivity.activity.baseContext
        hiltAndroidRule.inject()
        composeTestRuleActivity.waitForIdle()
    }

    @After
    fun tearDown() {
        database.clearAllTables()
    }

    @Test
    fun checkAllUIisDisplayedOnInitialAppOpen() {
        composeTestRuleActivity.let {
            it.onNodeWithText(baseContext.getString(R.string.app_name)).assertIsDisplayed()
            it.onNodeWithContentDescription(baseContext.getString(R.string.icon_cdescript)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.no_categories_found)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.create_category)).assertIsDisplayed()
        }
    }

    @Test
    fun displayCreateCategoryDialogButtonsFromTopAppBarButton() {
        composeTestRuleActivity.let {
            it.onNodeWithContentDescription(baseContext.getString(R.string.icon_cdescript)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.category)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.topic)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.lists_for)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.cancel)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.create)).assertIsDisplayed()
        }
    }

    @Test
    fun displayCreateCategoryDialogButtonsFromCenterButton() {
        composeTestRuleActivity.waitUntilAtLeastOneExists(hasText(baseContext.getString(R.string.create_category)), 3000)
        composeTestRuleActivity.let {
            it.onNodeWithText(baseContext.getString(R.string.create_category)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.category)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.topic)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.lists_for)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.cancel)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.create)).assertIsDisplayed()
        }
    }

    @Test
    fun dismissCreateCategoryDialogButtonsFromCenterButton() {
        composeTestRuleActivity.waitUntilAtLeastOneExists(hasText(baseContext.getString(R.string.create_category)), delayForUiVisibility)
        composeTestRuleActivity.let {
            it.onNodeWithText(baseContext.getString(R.string.create_category)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.create)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.cancel)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.create)).assertIsNotDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.no_categories_found)).assertIsDisplayed()
        }
    }

    @Test
    fun clickTextFieldsToShowPlaceHolderForCreateCategory() {
        composeTestRuleActivity.waitUntilAtLeastOneExists(hasText(baseContext.getString(R.string.create_category)), delayForUiVisibility)
        composeTestRuleActivity.let {
            it.onNodeWithText(baseContext.getString(R.string.create_category)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.category)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.category_placeholder)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.topic)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.topic_placeholder)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.lists_for)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.lists_for_placeholder)).assertIsDisplayed()
        }
    }

    @Test
    fun showBlankCategoryMessage() {
        composeTestRuleActivity.waitUntilAtLeastOneExists(hasText(baseContext.getString(R.string.create_category)), delayForUiVisibility)
        composeTestRuleActivity.let{
            it.onNodeWithText(baseContext.getString(R.string.create_category)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.create)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.error_message_no_category)).assertIsDisplayed()
        }
    }

    @Test
    fun showNoInitialListMessage() {
        composeTestRuleActivity.waitUntilAtLeastOneExists(hasText(baseContext.getString(R.string.create_category)), delayForUiVisibility)
        composeTestRuleActivity.let {
            it.onNodeWithText(baseContext.getString(R.string.create_category)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.category)).performTextInput("Treats")
            it.onNodeWithText(baseContext.getString(R.string.topic)).performTextInput("Dessert")
            it.onNodeWithText(baseContext.getString(R.string.create)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.error_message_no_initial_list)).assertIsDisplayed()
        }
    }

    @Test
    fun checkForSavedCategory() = runTest {
        composeTestRuleActivity.waitUntilAtLeastOneExists(hasText(baseContext.getString(R.string.create_category)), delayForUiVisibility)
        composeTestRuleActivity.let {
            it.onNodeWithText(baseContext.getString(R.string.create_category)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.category)).performTextInput("Treats")
            it.onNodeWithText(baseContext.getString(R.string.topic)).performTextInput("Dessert")
            it.onNodeWithText(baseContext.getString(R.string.lists_for)).performTextInput("Sundae")

            it.onNodeWithText(baseContext.getString(R.string.create)).performClick()
            it.onNodeWithText("Treats").assertIsDisplayed()
        }
    }

    @Test
    fun showDuplicateCategoryError() = runTest {
        composeTestRuleActivity.waitUntilAtLeastOneExists(hasText(baseContext.getString(R.string.create_category)), delayForUiVisibility)
        composeTestRuleActivity.let {
            it.onNodeWithText(baseContext.getString(R.string.create_category)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.category)).performTextInput("Treats")
            it.onNodeWithText(baseContext.getString(R.string.topic)).performTextInput("Dessert")
            it.onNodeWithText(baseContext.getString(R.string.lists_for)).performTextInput("Sundae")
            it.onNodeWithText(baseContext.getString(R.string.create)).performClick()

            it.onNodeWithContentDescription(baseContext.getString(R.string.icon_cdescript)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.category)).performTextInput("Treats")
            it.onNodeWithText(baseContext.getString(R.string.topic)).performTextInput("Tasty")
            it.onNodeWithText(baseContext.getString(R.string.lists_for)).performTextInput("Hummmm")
            it.onNodeWithText(baseContext.getString(R.string.create)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.error_message_duplicate_category)).assertIsDisplayed()
        }
    }

    @Test
    fun showDeletionDialog() = runTest {
        composeTestRuleActivity.waitUntilAtLeastOneExists(hasText(baseContext.getString(R.string.create_category)), delayForUiVisibility)
        composeTestRuleActivity.let {
            it.onNodeWithText(baseContext.getString(R.string.create_category)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.category)).performTextInput("Treats")
            it.onNodeWithText(baseContext.getString(R.string.topic)).performTextInput("Dessert")
            it.onNodeWithText(baseContext.getString(R.string.lists_for)).performTextInput("Sundae")
            it.onNodeWithText(baseContext.getString(R.string.create)).performClick()

            it.onNodeWithContentDescription(
                baseContext.getString(R.string.icon_delete_cdescript)
            ).assertIsDisplayed().performClick()

            it.onNodeWithContentDescription(baseContext.getString(R.string.icon_delete_dialog_cdescript)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.deletion_message) + "Treats").assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.cancel)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.delete)).assertIsDisplayed()
        }
    }

    @Test
    fun checkCategoryDeletionFromDialog() = runTest {
        composeTestRuleActivity.waitUntilAtLeastOneExists(hasText(baseContext.getString(R.string.create_category)), delayForUiVisibility)
        composeTestRuleActivity.let {
            it.onNodeWithText(baseContext.getString(R.string.create_category)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.category)).performTextInput("Treats")
            it.onNodeWithText(baseContext.getString(R.string.lists_for)).performTextInput("Sundae")
            it.onNodeWithText(baseContext.getString(R.string.create)).performClick()

            it.onNodeWithContentDescription(
                baseContext.getString(R.string.icon_delete_cdescript)
            ).assertIsDisplayed().performClick()

            it.onNodeWithText(baseContext.getString(R.string.cancel)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.deletion_message) + "Treats").assertIsNotDisplayed()
        }
    }

    @Test
    fun checkDeletionOfLastCategoryShowProperMessage() = runTest {
        composeTestRuleActivity.waitUntilAtLeastOneExists(hasText(baseContext.getString(R.string.create_category)), delayForUiVisibility)
        composeTestRuleActivity.let {
            it.onNodeWithText(baseContext.getString(R.string.create_category)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.category)).performTextInput("Treats")
            it.onNodeWithText(baseContext.getString(R.string.lists_for)).performTextInput("Sundae")
            it.onNodeWithText(baseContext.getString(R.string.create)).performClick()

            it.onNodeWithContentDescription(
                baseContext.getString(R.string.icon_delete_cdescript)
            ).assertIsDisplayed().performClick()

            it.onNodeWithText(baseContext.getString(R.string.delete)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.create_category)).assertIsDisplayed()
        }
    }
}