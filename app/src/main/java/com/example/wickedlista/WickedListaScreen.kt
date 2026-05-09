package com.example.wickedlista

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wickedlista.database.saveditems.StatusType
import com.example.wickedlista.ui.screens.AddItemScreen
import com.example.wickedlista.ui.screens.EditItemScreen
import com.example.wickedlista.ui.screens.HomeScreen
import com.example.wickedlista.ui.screens.SavedListScreen
import com.example.wickedlista.ui.viewmodels.HomeScreenViewModel


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

@Composable
fun CommonFormTextField(
    @StringRes label: Int,
    @StringRes hint: Int = R.string.empty_string,
    textFieldState: TextFieldState,
    modifier: Modifier = Modifier,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    isError: Boolean = false
) {
    OutlinedTextField(
        label = { Text(text = stringResource(label)) },
        placeholder = { Text(text = stringResource(hint)) },
        state = textFieldState,
        modifier = modifier,
        lineLimits = lineLimits,
        isError = isError,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Black,
            focusedLabelColor = Color.Black,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
        )
    )
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