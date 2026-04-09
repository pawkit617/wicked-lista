package com.example.wickedlista.ui.screens

import android.content.Context
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.wickedlista.HiltTestActivity
import com.example.wickedlista.R
import com.example.wickedlista.database.WickedListaDatabase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.theories.suppliers.TestedOn
import javax.inject.Inject

@HiltAndroidTest
class ModifyItemScreenTest {
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
    }

    private fun setUpAddScreen(isAddingMore: Boolean) {
        composeTestRuleActivity.setContent {
            AddItemScreen(
                ownerId = 1,
                isAddingMore = isAddingMore,
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

    @Test
    fun showInitialUiFeatures() {
        setUpAddScreen(false)
        composeTestRuleActivity.let {
            it.onNodeWithText(baseContext.getString(R.string.add_item_label)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.add_item_description_label)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.add_item_status_help_message)).assertIsDisplayed()
            it.onAllNodesWithText(baseContext.getString(R.string.add_item_additional_status_label)).assertCountEquals(3)
            it.onNodeWithText(baseContext.getString(R.string.create)).assertIsDisplayed()
        }
    }
}