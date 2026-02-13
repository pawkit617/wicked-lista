package com.example.wickedlista

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wickedlista.ui.screens.HomeScreen


enum class WickedListaScreen(@StringRes val title: Int) {
    HomeScreen(title = R.string.app_name),
    CreateList(title = R.string.create_list),
    AddItem(title = R.string.add_item)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WickedListaApp(
    navController: NavHostController = rememberNavController()
) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = WickedListaScreen.valueOf(
        backStackEntry?.destination?.route ?: WickedListaScreen.HomeScreen.name
    )
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopWickedListaAppBar(
                currentScreen = currentScreen,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.fillMaxSize()) {
            //config navHost
            NavHost(
                navController = navController,
                startDestination = WickedListaScreen.HomeScreen.name, //why not current screen?
                modifier = Modifier.fillMaxSize()
                   // .verticalScroll(rememberScrollState()) //uncomment will lead to “Infinite Height” Crash in Jetpack since i have a LazyVertcialGrid in HomeScreen
                    .padding(innerPadding)
            ) {
                composable(route = WickedListaScreen.HomeScreen.name) {
                    HomeScreen(
//                        createNewList = {
//                            navController.navigate(WickedListaScreen.CreateList.name)
//                        },
                        contentPaddingValues = innerPadding
                    )
                }
//                composable (route = WickedListaScreen.CreateList.name){
//                    NewListCreation()
//                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopWickedListaAppBar(
    currentScreen: WickedListaScreen,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = stringResource(currentScreen.title))
        },
        actions = {
            IconButton(
                onClick = {}
            ) {
                //Icon(imageVector = Icons.Defaults)
            }
        }
    )
}
