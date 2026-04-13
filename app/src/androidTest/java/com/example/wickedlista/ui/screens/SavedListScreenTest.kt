package com.example.wickedlista.ui.screens

import android.content.Context
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.wickedlista.HiltTestActivity
import com.example.wickedlista.R
import com.example.wickedlista.database.WickedListaDatabase
import com.example.wickedlista.database.homecategories.HomeCategories
import com.example.wickedlista.database.saveditems.SavedItems
import com.example.wickedlista.database.savedlists.SavedLists
import com.example.wickedlista.ui.theme.WickedListaTheme
import com.example.wickedlista.ui.viewmodels.SavedListViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@OptIn(ExperimentalTestApi::class)
class SavedListScreenTest {
    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule
    var composeTestRuleActivity = createAndroidComposeRule<HiltTestActivity>()

    private lateinit var baseContext: Context

    @Inject
    lateinit var wickedListaDatabase: WickedListaDatabase

    private val delayForUiVisibility = 1000L

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        baseContext = composeTestRuleActivity.activity.baseContext

        runBlocking {
            val homeCategories = HomeCategories(category = "Desserts", topic = "Treats")
            val savedLists = SavedLists(homeCategoriesForeignId = 1, owner = "Sundae")
            wickedListaDatabase.homeCategoriesDao().insertNewHomeCategories(homeCategories)
            wickedListaDatabase.homeCategoriesDao().insertInitialListForCategory(savedLists)
        }


    }

    private fun setUpScreenWithSavedLists() {
        runBlocking {
            val savedItems = SavedItems(
                savedItemId = 1,
                savedListForeignId = 1,
                label = "Ice Cream",
                description = "Vanilla",
                status = "Buy"
            )
            wickedListaDatabase.savedItemsDao().addItemToList(savedItems)
        }
    }
    private fun setUpScreenContent() {
        composeTestRuleActivity.setContent {
            WickedListaTheme() {
                SavedListScreen(
                    categoryId = 1,
                    topicName = "Treats",
                    savedListViewModel = hiltViewModel(),
                    onAddItemClick = { ownerId, isAddingMore -> },
                    onEditIconButtonClick = { savedItemId, savedItemLabel, savedItemDesc, currentStatus, ownerId -> }
                )
            }
        }
        composeTestRuleActivity.waitForIdle()
    }

    @After
    fun tearDown() {
        wickedListaDatabase.clearAllTables()
    }

    @Test
    fun checkUiWhenEnteringScreenWithNoSavedItems() {
        setUpScreenContent()
        composeTestRuleActivity.waitUntilAtLeastOneExists(hasText(baseContext.getString(R.string.topic_subtitle) + "Treats"), delayForUiVisibility)
        composeTestRuleActivity.let {
            it.onNodeWithText(baseContext.getString(R.string.topic_subtitle) + "Treats").assertIsDisplayed()
            it.onNodeWithText("Sundae").assertIsDisplayed()
            it.onNodeWithContentDescription(baseContext.getString(R.string.icon_add_listitem_cdescript)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.add_items_hint)).assertIsDisplayed()
            it.onNodeWithContentDescription(baseContext.getString(R.string.add_owner_button_cdescript)).assertIsDisplayed()
            it.onNodeWithContentDescription(baseContext.getString(R.string.icon_add_listitem_button_cdescript)).assertIsDisplayed()
            it.onNodeWithContentDescription(baseContext.getString(R.string.icon_delete_owner_button_cdescript)).assertIsDisplayed()
        }
    }

    @Test
    fun showAddNewOwnerDialogWithCancelClick() {
        setUpScreenContent()
        composeTestRuleActivity.waitUntilAtLeastOneExists(hasContentDescription(baseContext.getString(R.string.add_owner_button_cdescript)), delayForUiVisibility)
        composeTestRuleActivity.let {
            it.onNodeWithContentDescription(baseContext.getString(R.string.add_owner_button_cdescript)).performClick()
            it.onAllNodesWithContentDescription(baseContext.getString(R.string.icon_add_listitem_cdescript))[1].assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.add_owner_message)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.new_owner)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.add_owner)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.cancel)).assertIsDisplayed().performClick()
            it.onNodeWithText(baseContext.getString(R.string.add_owner_message)).assertIsNotDisplayed()
        }
    }

    @Test
    fun showAddNewOwnerDialogWithEmptyOwnerError() {
        setUpScreenContent()
        composeTestRuleActivity.waitUntilAtLeastOneExists(
            hasContentDescription(baseContext.getString(R.string.add_owner_button_cdescript)),
                delayForUiVisibility
                )
        composeTestRuleActivity.let {
            it.onNodeWithContentDescription(baseContext.getString(R.string.add_owner_button_cdescript)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.add_owner)).assertIsDisplayed().performClick()
            it.onNodeWithText(baseContext.getString(R.string.error_message_no_owner)).assertIsDisplayed()
        }
    }

    @Test
    fun addOwnerSuccessfullyClick() = runTest {
        setUpScreenContent()
        composeTestRuleActivity.waitUntilAtLeastOneExists(
            hasContentDescription(baseContext.getString(R.string.add_owner_button_cdescript)),
            delayForUiVisibility
        )
        composeTestRuleActivity.let {
            it.onNodeWithContentDescription(baseContext.getString(R.string.add_owner_button_cdescript)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.new_owner)).performTextInput("Cake")
            it.onNodeWithText(baseContext.getString(R.string.add_owner)).assertIsDisplayed().performClick()
            it.onNodeWithText("Cake").assertIsDisplayed()
        }
    }

    @Test
    fun showDeleteOwnerWarningDialog() {
        setUpScreenContent()
        composeTestRuleActivity.let {
            it.onNodeWithContentDescription(baseContext.getString(R.string.icon_delete_owner_button_cdescript))
                .performClick()
            it.onNodeWithContentDescription(baseContext.getString(R.string.icon_delete_owner_cdescript))
                .assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.delete_last_owner_message)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.ok)).assertIsDisplayed()
        }
    }

    @Test
    fun showDeletionOwnerDialog() = runTest {
        setUpScreenContent()
        composeTestRuleActivity.let {
            it.onNodeWithContentDescription(baseContext.getString(R.string.add_owner_button_cdescript)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.new_owner)).performTextInput("Cake")
            it.onNodeWithText(baseContext.getString(R.string.add_owner)).assertIsDisplayed().performClick()
            it.waitForIdle()
            it.onNodeWithText("Cake").assertIsDisplayed()

            it.onNodeWithContentDescription(baseContext.getString(R.string.icon_delete_owner_button_cdescript))
                .performClick()
            it.onNodeWithContentDescription(baseContext.getString(R.string.icon_delete_owner_cdescript))
                .assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.delete_owner_message, "Cake")).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.delete)).assertIsDisplayed().performClick()
            it.waitForIdle()
            it.onNodeWithText("Cake").assertIsNotDisplayed()
        }
    }

    @Test
    fun showListOfSavedItems() = runTest {
        setUpScreenWithSavedLists()
        composeTestRuleActivity.waitForIdle()
        setUpScreenContent()
        composeTestRuleActivity.waitUntilAtLeastOneExists(hasText("Ice Cream"), delayForUiVisibility)
        composeTestRuleActivity.onNodeWithText("Ice Cream").assertIsDisplayed()
    }
}