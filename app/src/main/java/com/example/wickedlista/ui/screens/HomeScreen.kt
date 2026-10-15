package com.example.wickedlista.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.rememberUpdatedStyleState
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.wickedlista.CommonFormTextField
import com.example.wickedlista.R
import com.example.wickedlista.StyleButton
import com.example.wickedlista.data.HomeScreenUIState
import com.example.wickedlista.database.homecategories.HomeCategories
import com.example.wickedlista.ui.theme.getDialogStyle
import com.example.wickedlista.ui.theme.getHomeCardStyle
import com.example.wickedlista.ui.viewmodels.HomeScreenViewModel


@Composable
fun HomeScreen(
    homeScreenViewModel: HomeScreenViewModel,
    onClickOfHomeListCard: () -> Unit,
    contentPaddingValues: PaddingValues = PaddingValues(0.dp)
) {

    homeScreenViewModel.getLists()
    val homeScreenUIStateCollected = homeScreenViewModel.uiState.collectAsState()

    val showCreateDialog = homeScreenUIStateCollected.value.showCreateDialog
    val showDeletionDialog = homeScreenUIStateCollected.value.showDeletionDialog
    val showNoListMessage = homeScreenUIStateCollected.value.showNoListMessage
    val allLists = homeScreenUIStateCollected.value.existingHomeLists

    when {
        showCreateDialog -> CreateListDialog(homeScreenViewModel)
        showDeletionDialog -> DeletionDialog(homeScreenViewModel)
        showNoListMessage -> NoListFoundScreen(homeScreenViewModel, contentPaddingValues)
        else -> ListsScreen(allLists, homeScreenViewModel, onClickOfHomeListCard, contentPaddingValues)
    }
}

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun ListsScreen(
    allLists: List<HomeCategories>,
    homeScreenViewModel: HomeScreenViewModel,
    onHomeListClick: () -> Unit,
    contentPaddingValues: PaddingValues
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(200.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest), //.background(MaterialTheme.colorScheme.secondaryContainer)
        contentPadding = contentPaddingValues
    ) {
        items(items = allLists, key = {list -> list.id}) { list ->
            val interactionSource = remember { MutableInteractionSource() }
            val styleState = rememberUpdatedStyleState(interactionSource)
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .clickable(
                            onClick = {
                                homeScreenViewModel.setCurrentlySelectedHomeList(
                                    list.id,
                                    list.category,
                                    list.topic)
                                onHomeListClick()
                            },
                            interactionSource = interactionSource
                    )
                    .styleable(
                        styleState = styleState,
                        getHomeCardStyle(
                        MaterialTheme.shapes.medium,
                        MaterialTheme.colorScheme.secondaryContainer,
                        MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
            ) {
                Box {
                    IconButton(
                        onClick = {
                            homeScreenViewModel.setDeletionDialogVisibility(true)
                            homeScreenViewModel.setCurrentlySelectedHomeList(list.id, list.category, list.topic)
                        },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.close_small_24),
                            modifier = Modifier
                                .fillMaxSize()
                                .scale(.5f),
                            contentDescription = stringResource(R.string.icon_delete_cdescript),
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.category_24dp),
                            contentScale = ContentScale.None,
                            modifier = Modifier
                                .size(64.dp)
                                .padding(8.dp)
                                .clip(MaterialTheme.shapes.small)
                                .background(Color.White),
                            contentDescription = stringResource(R.string.category_icon)
                        )
                        Text(text = list.category)
                        Text(text = list.topic, modifier = Modifier.padding(bottom = 8.dp))
                    }
                }
            }
        }
   }
}

@Composable
fun NoListFoundScreen(
    homeScreenViewModel: HomeScreenViewModel,
    contentPaddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPaddingValues)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(0.dp, 0.dp,0.dp, 16.dp),
            text = stringResource(R.string.no_categories_found),
            style = MaterialTheme.typography.titleLarge
        )

        StyleButton(
            onClick = { homeScreenViewModel.setCreateDialogVisibility(true) },
            text = stringResource(R.string.create_category),
            backgroundColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.surfaceDim
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeletionDialog(homeScreenViewModel: HomeScreenViewModel) {
    BasicAlertDialog(
        onDismissRequest = {homeScreenViewModel.setDeletionDialogVisibility(false)},
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = true
        )
    ) {
        val current = homeScreenViewModel.uiState.collectAsState().value.currentlySelectedHomeList

        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.list_remove_48),
                    modifier = Modifier
                        .size(48.dp)
                        .padding(bottom = 8.dp),
                    contentDescription = stringResource(R.string.icon_delete_dialog_cdescript)
                )
                Text(
                    text = stringResource(R.string.deletion_message) + current.second,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth() //Curt - necessary if u want SpaceEvenly to work
                ) {
                    StyleButton(
                        onClick = { homeScreenViewModel.setDeletionDialogVisibility(false) },
                        text = stringResource(R.string.cancel)
                    )
                    StyleButton(
                        onClick = {
                            homeScreenViewModel.deleteHomeList()
                            homeScreenViewModel.setDeletionDialogVisibility(false)
                        },
                        text = stringResource(R.string.delete),
                        backgroundColor = MaterialTheme.colorScheme.onErrorContainer,
                        contentColor = MaterialTheme.colorScheme.surfaceDim,
                        pressedColor = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun CreateListDialog(homeScreenViewModel: HomeScreenViewModel) {
    val homeScreenUIState = homeScreenViewModel.uiState.collectAsState() //CURT - will not be call on state change. Seems state change is only recognized in HomeScreen(...)
    val shouldShow = homeScreenUIState.value.showCreateDialog
    if (shouldShow) {
        val titleState = homeScreenViewModel.categorySavedState
        val topicState = homeScreenViewModel.topicSavedState
        val listForState = homeScreenViewModel.listForSavedState

        Dialog(
            onDismissRequest = {}
        ) {
            Card {
                Column(
                    modifier = Modifier
                        .styleable(
                            null,
                            getDialogStyle(
                                MaterialTheme.colorScheme.surfaceDim
                            )
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CommonFormTextField(
                        R.string.category,
                        titleState,
                        isError = homeScreenUIState.value.isError,
                    )
                    CommonFormTextField(
                        R.string.topic,
                        topicState,
                    )

                    CommonFormTextField(
                        R.string.lists_for,
                        listForState,
                        isError = homeScreenUIState.value.hasNoListFor
                    )

                    ErrorMessageDisplayWithinDialog(homeScreenUIState)

                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, start = 0.dp, end = 0.dp, bottom = 0.dp)
                    ) {
                        StyleButton(
                            onClick = {
                                homeScreenViewModel.setCreateDialogVisibility(false)
                                homeScreenViewModel.clearErrors()
                                homeScreenViewModel.clearTextFieldStates()
                            },
                            text = stringResource(R.string.cancel)
                        )
                        StyleButton(
                            onClick = {
                                homeScreenViewModel.createNewList(
                                    titleState.text,
                                    topicState.text,
                                    listForState.text
                                )
                            },
                            text = stringResource(R.string.create),
                            backgroundColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.surfaceDim
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorMessageDisplayWithinDialog(homeScreenUIState: State<HomeScreenUIState>) {
    val errMsg = when {
        homeScreenUIState.value.isError -> R.string.error_message_no_category
        homeScreenUIState.value.hasSQLError -> R.string.error_message_duplicate_category
        homeScreenUIState.value.hasNoListFor -> R.string.error_message_no_initial_list
        else -> -1
    }

    if (errMsg != -1) {
        Text(
            text = stringResource(errMsg),
            color = MaterialTheme.colorScheme.error)

    }
}
