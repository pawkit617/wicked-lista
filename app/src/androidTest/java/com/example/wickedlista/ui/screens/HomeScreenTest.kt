package com.example.wickedlista.ui.screens

import android.content.Context
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wickedlista.HiltTestActivity
import com.example.wickedlista.MainActivity

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

import com.example.wickedlista.R
import com.example.wickedlista.WickedListaApp
import com.example.wickedlista.ui.theme.WickedListaTheme
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeScreenTest {
    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule
    var composeTestRuleActivity = createAndroidComposeRule<MainActivity>()

    private lateinit var baseContext: Context

    @Before
    fun setUp() {
        baseContext = composeTestRuleActivity.activity.baseContext
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
            it.onNodeWithText(baseContext.getString(R.string.cancel)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.create)).assertIsDisplayed()
        }
    }

    @Test
    fun displayCreateCategoryDialogButtonsFromCenterButton() {
        composeTestRuleActivity.let {
            it.onNodeWithText(baseContext.getString(R.string.create_category)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.cancel)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.create)).assertIsDisplayed()
        }
    }

    @Test
    fun dismissCreateCategoryDialogButtonsFromCenterButton() {
        composeTestRuleActivity.let {
            it.onNodeWithText(baseContext.getString(R.string.create_category)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.create)).assertIsDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.cancel)).performClick()
            it.onNodeWithText(baseContext.getString(R.string.create)).assertIsNotDisplayed()
            it.onNodeWithText(baseContext.getString(R.string.no_categories_found)).assertIsDisplayed()
        }
    }
}