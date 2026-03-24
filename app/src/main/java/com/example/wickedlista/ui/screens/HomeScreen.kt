package com.example.wickedlista.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
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
import com.example.wickedlista.R
import com.example.wickedlista.data.HomeScreenUIState
import com.example.wickedlista.database.homecategories.HomeCategories
import com.example.wickedlista.ui.viewmodels.HomeScreenViewModel


@Composable
fun HomeScreen(
    homeScreenViewModel: HomeScreenViewModel,
    onClickOfHomeListCard: () -> Unit,
    modifier: Modifier = Modifier,
    contentPaddingValues: PaddingValues = PaddingValues(0.dp)
) {

    homeScreenViewModel.getLists() //CURT - move this to WickedListaScreen?
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
            .padding( 8.dp),
        contentPadding = contentPaddingValues
    ) {
        items(items = allLists, key = {list -> list.id}) { list ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                onClick = {
                    homeScreenViewModel.setCurrentlySelectedHomeList(list.id, list.category)
                    onHomeListClick()
                }
            ) {
                Box {
                    IconButton(
                        onClick = {
                            homeScreenViewModel.setDeletionDialogVisibility(true)
                            homeScreenViewModel.setCurrentlySelectedHomeList(list.id, list.category)
                        },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.close_small_24),
                            modifier = Modifier.fillMaxSize().scale(.5f),
                            contentDescription = stringResource(R.string.icon_delete_cdescript),
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.rounded_chess_queen_24),
                            contentScale = ContentScale.None,
                            modifier = Modifier
                                .size(64.dp)
                                .padding(8.dp)
                                .clip(MaterialTheme.shapes.small)
                                .background(Color.White),
                            contentDescription = ""
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
            .padding(contentPaddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(0.dp, 0.dp,0.dp, 16.dp),
            text = stringResource(R.string.no_categories_found),
            style = MaterialTheme.typography.titleLarge
        )
        Button(
            shape = MaterialTheme.shapes.small,
            onClick = {
                homeScreenViewModel.setCreateDialogVisibility(true)
            }
        ) {
            Text(
                text = stringResource(R.string.create_category),
                style = MaterialTheme.typography.labelLarge)
        }
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
                    contentDescription = stringResource(R.string.icon_delete_cdescript)
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
                    Button(onClick = { homeScreenViewModel.setDeletionDialogVisibility(false) }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    Button(
                        onClick = {
                            homeScreenViewModel.deleteHomeList()
                            homeScreenViewModel.setDeletionDialogVisibility(false)
                        }
                    ) {
                        Text(text = stringResource(R.string.delete))
                    }
                }
            }
        }
    }
}

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
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        state = titleState,
                        label = { Text(text = stringResource(R.string.category)) },
                        placeholder = { Text(stringResource(R.string.category_placeholder)) },
                        isError = homeScreenUIState.value.isError,
                        lineLimits = TextFieldLineLimits.SingleLine,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(Modifier.padding(8.dp))

                    OutlinedTextField(
                        state = topicState,
                        label = { Text(text = stringResource(R.string.topic)) },
                        lineLimits = TextFieldLineLimits.SingleLine,
                        placeholder = { Text(text = stringResource(R.string.topic_placeholder)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.padding(8.dp))

                    OutlinedTextField(
                        state = listForState,
                        label = { Text(text = stringResource(R.string.lists_for)) },
                        isError = homeScreenUIState.value.hasNoListFor,
                        lineLimits = TextFieldLineLimits.SingleLine,
                        placeholder = { Text(text = stringResource(R.string.lists_for_placeholder)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.padding(8.dp))
                    ErrorMessageDisplayWithinDialog(homeScreenUIState)

                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                homeScreenViewModel.setCreateDialogVisibility(false)
                                homeScreenViewModel.clearErrors()
                                homeScreenViewModel.clearTextFieldStates()
                            },
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(text = stringResource(R.string.cancel))
                        }
                        Button(
                            onClick = {
                                homeScreenViewModel.createNewList(
                                    titleState.text,
                                    topicState.text,
                                    listFor = listForState.text
                                )
                            },
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = stringResource(R.string.create)
                            )
                        }
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
            color = Color.Red)
    }
}
