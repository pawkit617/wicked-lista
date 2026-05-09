package com.example.wickedlista.ui.screens

import android.content.Context
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.wickedlista.HiltTestActivity
import com.example.wickedlista.R
import com.example.wickedlista.database.WickedListaDatabase
import com.example.wickedlista.database.homecategories.HomeCategories
import com.example.wickedlista.database.itemstatus.ItemStatus
import com.example.wickedlista.database.itemstatuschecked.ItemStatusChecked
import com.example.wickedlista.database.savedlists.SavedLists
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
class ModifyItemScreenTest {
    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule
    var composeTestRuleActivity = createAndroidComposeRule<HiltTestActivity>()

    private lateinit var baseContext: Context

    @Inject
    lateinit var wickedListaDatabase: WickedListaDatabase

    private val delayForUiVisibility = 3000L

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        baseContext = composeTestRuleActivity.activity.baseContext
        runBlocking {
            wickedListaDatabase.homeCategoriesDao().insertNewHomeCategories(
                HomeCategories(
                    category = "Desserts",
                    topic = "Treats"
                )
            )
            wickedListaDatabase.homeCategoriesDao().insertInitialListForCategory(
                SavedLists(
                    homeCategoriesForeignId = 1,
                    owner = "Sundae"
                )
            )
            wickedListaDatabase.savedListsDao().addOwnerWithCategoryId(
                SavedLists(
                    homeCategoriesForeignId = 1,
                    owner = "Dessert"
                )
            )
        }

    }

    @After
    fun tearDown() {
        wickedListaDatabase.clearAllTables()
    }

    private fun setUpAddScreen() {
        composeTestRuleActivity.setContent {
            AddItemScreen(
                ownerId = 1,
                onDoneAddingItems = {},
                modifyItemViewModel = hiltViewModel()
            )
        }
        composeTestRuleActivity.waitForIdle()
    }

    private fun setUpEditScreen() {
        composeTestRuleActivity.setContent {
            EditItemScreen(
                savedItemId = 1,
                savedItemLabel = "Flour",
                savedItemDesc = "Gold Medal",
                currentStatus = "Need",
                ownerId = 1,
                onDoneEditingItems = {},
                modifyItemViewModel = hiltViewModel()
            )
        }
        composeTestRuleActivity.waitForIdle()
    }

    private fun setStatusInDatabase() {
        val itemStatus = ItemStatus(
            savedListForeignId = 1,
            firstStatus = "Need",
            secondStatus = "Bought",
            thirdStatus = "Grow",
            fourthStatus = "Trade"
        )
        runBlocking {
            wickedListaDatabase.itemStatusDao().addItemStatus(itemStatus)
        }
    }

    @Test
    fun showInitialUiFeatures() {
        setUpAddScreen()
        composeTestRuleActivity.let {
            it.onNodeWithText(baseContext.getString(R.string.add_item_label)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.add_item_description_label)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.add_item_checkbox_toggle_message)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.add_item_status_help_message)).assertIsDisplayed()
            it.onAllNodesWithText(baseContext.getString(R.string.add_item_additional_status_label)).assertCountEquals(3)
            it.onNodeWithText(baseContext.getString(R.string.create)).assertIsDisplayed()
        }
    }

    @Test
    fun showInitialUiFeaturesForCheckbox() {
        setUpAddScreen()
        composeTestRuleActivity.let {
            it.onNodeWithText(baseContext.getString(R.string.add_item_checkbox_toggle_message)).assertIsDisplayed()
            it.onNodeWithContentDescription(
                baseContext.getString(R.string.switch_for_checkbox_cdescript)
            ).assertIsDisplayed().performClick()
            it.onNodeWithText(baseContext.getString(R.string.add_item_checkbox_toggle_message)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.add_item_status_choose_message)).assertIsNotDisplayed()
        }
    }

    @Test
    fun showStatusAsCheckbox() {
       // setItemStatusCheckedInDatabase()
        setUpAddScreen()
        composeTestRuleActivity.let {
            it.onNodeWithText(baseContext.getString(R.string.add_item_label)).performTextInput("Lemons")
            it.onNodeWithContentDescription(
                baseContext.getString(R.string.switch_for_checkbox_cdescript)
            ).assertIsDisplayed().performClick()
            it.onNodeWithText(baseContext.getString(R.string.add_item_checkbox_label)).performTextInput("Bought")
            it.onNodeWithText(baseContext.getString(R.string.create)).performClick()

            it.onNodeWithText("Bought").assertIsDisplayed()
            it.onNodeWithContentDescription(baseContext.getString(R.string.status_checkbox_cdescript)).performClick()
        }
    }

    @Test
    fun showBlankLabelError() {
        setUpAddScreen()
        composeTestRuleActivity.let {
            it.onNodeWithText(baseContext.getString(R.string.create)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.error_add_item_blank_label)).assertIsDisplayed()
        }
    }

    @Test
    fun showBlankStatusError() {
        setUpAddScreen()
        composeTestRuleActivity.let {
            it.onNodeWithText(baseContext.getString(R.string.add_item_label)).performTextInput("Sugar")
            it.onNodeWithText(baseContext.getString(R.string.create)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.error_add_item_blank_status)).assertIsDisplayed()

        }
    }

    @Test
    fun showAddItemSuccessDialog() = runTest {
        setUpAddScreen()
        composeTestRuleActivity.let {
            it.onNodeWithText(baseContext.getString(R.string.add_item_label)).performTextInput("Sugar")
            it.onNodeWithText(baseContext.getString(R.string.add_item_description_label)).performTextInput("Powder")
            it.onNodeWithText(baseContext.getString(R.string.add_item_initial_status_label)).performTextInput("Need")
            it.onNodeWithText(baseContext.getString(R.string.create)).performClick()
            it.waitUntilAtLeastOneExists(hasContentDescription(baseContext.getString(R.string.add_item_success_message_icon)), delayForUiVisibility)
            it.onNodeWithContentDescription(baseContext.getString(R.string.add_item_success_message_icon)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.add_item_successful_add_message)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.add_more)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.done)).assertIsDisplayed()
        }
    }

    @Test
    fun showAddItemSuccessDialogAndClickAddMore() = runTest {
        setUpAddScreen()
        composeTestRuleActivity.let {
            it.onNodeWithText(baseContext.getString(R.string.add_item_label)).performTextInput("Sugar")
            it.onNodeWithText(baseContext.getString(R.string.add_item_description_label)).performTextInput("Powder")
            it.onNodeWithText(baseContext.getString(R.string.add_item_initial_status_label)).performTextInput("Need")
            it.onNodeWithText(baseContext.getString(R.string.create)).performClick()
            it.waitUntilAtLeastOneExists(hasContentDescription(baseContext.getString(R.string.add_item_success_message_icon)), delayForUiVisibility)
            it.onNodeWithContentDescription(baseContext.getString(R.string.add_item_success_message_icon)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.add_item_successful_add_message)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.add_more)).assertIsDisplayed().performClick()
        }
    }

    @Test
    fun showAddItemSuccessDialogAndClickDone() = runTest {
        setUpAddScreen()
        composeTestRuleActivity.let {
            it.onNodeWithText(baseContext.getString(R.string.add_item_label)).performTextInput("Sugar")
            it.onNodeWithText(baseContext.getString(R.string.add_item_description_label)).performTextInput("Powder")
            it.onNodeWithText(baseContext.getString(R.string.add_item_initial_status_label)).performTextInput("Need")
            it.onNodeWithText(baseContext.getString(R.string.create)).performClick()
            it.waitUntilAtLeastOneExists(hasContentDescription(baseContext.getString(R.string.add_item_success_message_icon)), delayForUiVisibility)
            it.onNodeWithContentDescription(baseContext.getString(R.string.add_item_success_message_icon)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.add_item_successful_add_message)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.done)).assertIsDisplayed().performClick()
        }
    }

    @Test
    fun showEditScreen() {
        setStatusInDatabase()
        setUpEditScreen()
        composeTestRuleActivity.let {
            it.onNodeWithText(baseContext.getString(R.string.add_item_label)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.add_item_description_label)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.add_item_status_choose_message)).assertIsDisplayed()
            it.onNodeWithContentDescription(baseContext.getString(R.string.status_menu_drop_down_icon)).performClick()
            it.onNodeWithText("Bought").assertIsDisplayed().performClick()
        }
    }

    @Test
    fun clickDelete() {
        setStatusInDatabase()
        setUpEditScreen()
        composeTestRuleActivity.onNodeWithText(baseContext.getString(R.string.delete)).performClick()

    }

    @Test
    fun clickUpdate() {
        setStatusInDatabase()
        setUpEditScreen()
        composeTestRuleActivity.onNodeWithText(baseContext.getString(R.string.update)).performClick()
    }
}