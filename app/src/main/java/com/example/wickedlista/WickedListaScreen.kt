package com.example.wickedlista

import androidx.annotation.StringRes
import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.MutableStyleState
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.contentPadding
import androidx.compose.foundation.style.fillWidth
import androidx.compose.foundation.style.pressed
import androidx.compose.foundation.style.rememberUpdatedStyleState
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.BadgeDefaults.containerColor
import androidx.compose.material3.BottomAppBarDefaults.containerColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wickedlista.ui.screens.AddItemScreen
import com.example.wickedlista.ui.screens.EditItemScreen
import com.example.wickedlista.ui.screens.HomeScreen
import com.example.wickedlista.ui.screens.SavedListScreen
import com.example.wickedlista.ui.theme.WickedListaTheme
import com.example.wickedlista.ui.theme.commonShadow
import com.example.wickedlista.ui.theme.getButtonStyle
import com.example.wickedlista.ui.theme.getCommonTextFieldStyle
import com.example.wickedlista.ui.viewmodels.HomeScreenViewModel
import kotlinx.coroutines.flow.Flow


enum class WickedListaScreen(@StringRes val title: Int, val path: String) {
    HomeScreen(title = R.string.app_name, path = "wickedLista"),
    SavedListScreen(title = R.string.saved_list_screen, path = "savedList"),
    AddItem(title = R.string.add_item, path = "addItem/{ownerId}"),
    EditItem(title = R.string.edit_item, path = "editItem/{savedItemId}/{savedItemLabel}/{savedItemDesc}/{currentStatus}/{ownerId}")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WickedListaApp(
    navController: NavHostController = rememberNavController(),
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val x = WickedListaScreen.entries.filter {
        it.path == backStackEntry?.destination?.route
    }

    val currentScreen = if(x.isEmpty()) WickedListaScreen.HomeScreen else x.first()

    TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        //modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), //CURT - uncomment makes nothing scrollable
        topBar = {
            TopWickedListaAppBar(
                currentScreen = currentScreen,
                homeScreenViewModel = homeScreenViewModel,
            )
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = WickedListaScreen.HomeScreen.path, //CURT - WickedListaScreen.HomeScreen.name why not current screen? B/C it has to be explicit
                modifier = Modifier.fillMaxSize()
                   //CURT -  .verticalScroll(rememberScrollState()) //uncomment will lead to “Infinite Height” Crash in Jetpack since i have a LazyVertcialGrid in HomeScreen
                    .padding(innerPadding)
            ) {
                composable(route = WickedListaScreen.HomeScreen.path) {
                    HomeScreen(
                        homeScreenViewModel = homeScreenViewModel,
                        onClickOfHomeListCard = {
                            navController.navigate(WickedListaScreen.SavedListScreen.path)
                        },
                        contentPaddingValues = PaddingValues(0.dp)
                    )
                }
                composable (route = WickedListaScreen.SavedListScreen.path) {
                    val homeScreenUIState by homeScreenViewModel.uiState.collectAsState()
                    SavedListScreen(
                        topicName = homeScreenUIState.currentlySelectedHomeList.third,
                        categoryId = homeScreenUIState.currentlySelectedHomeList.first,
                        onAddItemClick = { ownerId ->
                            navController.navigate("addItem/$ownerId")
                        },
                        onEditIconButtonClick = { savedItemId, savedItemLabel, savedItemDesc, currentStatus, ownerId ->
                            navController.navigate("editItem/$savedItemId/$savedItemLabel/$savedItemDesc/$currentStatus/$ownerId")
                        }
                    )
                }
                composable (
                    route = WickedListaScreen.AddItem.path,
                ) { backStackEntry ->
                    backStackEntry.arguments?.let {
                        val ownerId = it.getString("ownerId") ?: "-1"
                        AddItemScreen(
                            ownerId.toInt(),
                            {
                                navController.popBackStack(WickedListaScreen.SavedListScreen.path, false)
                            }
                        )
                    }
                }
                composable (route = WickedListaScreen.EditItem.path) { backStackEntry ->
                    backStackEntry.arguments?.let {
                        val ownerId = it.getString("ownerId") ?: ""
                        val savedItemId = it.getString("savedItemId") ?: "-1"
                        val savedItemLabel = it.getString("savedItemLabel") ?: ""
                        val savedItemDesc = it.getString("savedItemDesc") ?: ""
                        val currentStatus = it.getString("currentStatus") ?: ""
                        EditItemScreen(
                            savedItemId.toInt(), savedItemLabel,
                            savedItemDesc, currentStatus,
                            ownerId.toInt(),
                            {
                                navController.popBackStack(WickedListaScreen.SavedListScreen.path, false)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopWickedListaAppBar(
    currentScreen: WickedListaScreen,
    homeScreenViewModel: HomeScreenViewModel,
) {
    CenterAlignedTopAppBar(
        modifier = Modifier.dropShadow(
            RectangleShape,
            commonShadow
        ),
        title = {
            val titleOfList = if (!currentScreen.name.equals(WickedListaScreen.SavedListScreen.name) ) {
                stringResource(currentScreen.title)
            } else {
                homeScreenViewModel.uiState.collectAsState().value.currentlySelectedHomeList.second
            }
            Text(text = titleOfList)
        },
        actions = {
            if (currentScreen.name.equals(WickedListaScreen.HomeScreen.name)) {
                IconButton(
                    onClick = { homeScreenViewModel.setCreateDialogVisibility(true) }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add_24dp),
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = stringResource(R.string.icon_cdescript)
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationStyleApi::class, ExperimentalFoundationApi::class)
@Composable
fun CommonFormTextField(
    @StringRes hint: Int = R.string.empty_string,
    textFieldState: TextFieldState,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    isError: Boolean = false,
    insertSpacer: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val styleState = rememberUpdatedStyleState(interactionSource)
    val fontSize = MaterialTheme.typography.bodyLarge.fontSize
    val commonTextFieldStyle = getCommonTextFieldStyle(
        MaterialTheme.shapes.small,
        fontSize,
        MaterialTheme.colorScheme.surfaceContainerLowest,
        MaterialTheme.colorScheme.outlineVariant,
        if (isError) MaterialTheme.colorScheme.error else Color.Black,
        if (lineLimits != TextFieldLineLimits.SingleLine ) 100.dp else 50.dp
        )

    BasicTextField(
        state = textFieldState,
        modifier = Modifier.styleable(styleState, commonTextFieldStyle),
        lineLimits = lineLimits,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(Color.Black),
        textStyle = TextStyle(Color.Black, fontSize),
        decorator = { innerTextField ->
            Box (contentAlignment = Alignment.CenterStart) {
                if (textFieldState.text.isEmpty()) {
                    Text(
                        text = stringResource(hint)
                    )
                }
                innerTextField()
            }
        }
    )
    if (insertSpacer) {
        Spacer(
            Modifier.height(16.dp).fillMaxWidth()
        )
    }
}


@OptIn(ExperimentalFoundationStyleApi::class, ExperimentalFoundationApi::class)
@Composable
fun StyleButton(
    onClick : () -> Unit = {},
    text: String = "",
    contentColor: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    pressedColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    pressedContentColor: Color = MaterialTheme.colorScheme.inversePrimary,
    buttonWidth: Dp? = null
) {

    val interactionSource = remember { MutableInteractionSource() }
    val styleState = rememberUpdatedStyleState(interactionSource)
    val buttonStyle = getButtonStyle(
        contentColor,
        backgroundColor,
        pressedColor = pressedColor,
        pressedContentColor = pressedContentColor,
        MaterialTheme.shapes.small,
        buttonWidth
    )

    Row(
        modifier = Modifier
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
            )
            .styleable(
                styleState,
                buttonStyle
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = text
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShowMyPreview() {
    WickedListaTheme { //so preview matches colors in emulator
//        Row(
//            modifier = Modifier
//                .width(150.dp)
//                .height(100.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center
//        ) {
//            StyleButton(
//                onClick = {},
//                text = "Demo"
//            )
//        }

        Row(
            modifier = Modifier
                .width(150.dp)
                .height(100.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CommonFormTextField(
                hint = R.string.category_placeholder,
                textFieldState = TextFieldState(),
                isError = true
            )
        }


    }
}

@Composable
fun CommonButton(
    onClick : () -> Unit,
    text: String
) {
    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
        )
    ) {
        Text(text = text)
    }
}