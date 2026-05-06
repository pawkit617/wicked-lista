package com.example.wickedlista.ui.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.wickedlista.CommonButton
import com.example.wickedlista.CommonFormTextField
import com.example.wickedlista.R
import com.example.wickedlista.data.SavedListUIState
import com.example.wickedlista.database.saveditems.SavedItems
import com.example.wickedlista.database.saveditems.StatusType
import com.example.wickedlista.database.savedlists.SavedLists
import com.example.wickedlista.ui.viewmodels.SavedListViewModel

@Composable
fun SavedListScreen(
    categoryId: Int,
    topicName: String,
    savedListViewModel: SavedListViewModel = hiltViewModel(),
    onAddItemClick: (ownerId: Int) -> Unit,
    onEditIconButtonClick:(
        savedItemId: Int,savedItemLabel: String,
        savedItemDesc: String, currentStatus: String,
        ownerId: Int) -> Unit
) {
    val savedListViewModelState by savedListViewModel.uiState.collectAsState() //using 'by' allows u to avoid .value()
    LaunchedEffect(savedListViewModelState.allSavedLists.size) {
        savedListViewModel.getAllSavedListsForCategoryId(categoryId)
    }

    val listOfSavedListsOwners = savedListViewModelState.allSavedLists

    AddOwnerDialog(savedListViewModel, categoryId)
    WarningForDeletingLastOwnerDialog(savedListViewModel)
    DeleteOwnerDialog(savedListViewModelState.selectedOwner, savedListViewModel)
    OwnersWithListItems(topicName, listOfSavedListsOwners, savedListViewModel, onAddItemClick, onEditIconButtonClick)
}

// region Owners UI, Left Column
@Composable
fun OwnersWithListItems(
    topicName: String,
    listOfSavedListsOwners: List<SavedLists>,
    savedListViewModel: SavedListViewModel,
    onAddItemClick: (ownerId: Int) -> Unit,
    onEditIconButtonClick: (savedItemId: Int,savedItemLabel: String, savedItemDesc: String, currentStatus: String, ownerId: Int) -> Unit
) {
    if (listOfSavedListsOwners.isNotEmpty()) {
        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
            if (topicName.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.topic_subtitle) + topicName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color.White)
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .weight(0.9f)
            ) {
                OwnersOfSavedList(
                    listOfSavedListsOwners,
                    savedListViewModel,
                    Modifier
                        .weight(0.25f)
                        .background(color = Color.Gray)
                )
                ItemsOfSavedLists(
                    savedListViewModel,
                    onEditIconButtonClick,
                    Modifier
                        .weight(0.75f)
                        .background(color = Color.White)
                )
            }
            BottomButtonEditRow(
                savedListViewModel,
                Modifier
                    .fillMaxWidth()
                    .weight(0.1f),
                onAddItemClick
            )
        }
    }
}

@Composable
fun OwnersOfSavedList(savedLists: List<SavedLists>, savedListViewModel: SavedListViewModel, modifier: Modifier) {
    val currentlySelectedOwner = savedListViewModel.uiState.collectAsState().value.selectedOwner
    LazyColumn(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(savedLists) {
            if (it.savedListId == currentlySelectedOwner.first) {
                OwnerSelected(it)
            } else {
                OwnerUnSelected(it, savedListViewModel = savedListViewModel)
            }
        }
    }

}

@Composable
fun OwnerSelected(savedLists: SavedLists) {
    Button(
        onClick = {},
        shape = RoundedCornerShape(
            topStart = 10.dp,
            topEnd = 0.dp,
            bottomEnd = 0.dp,
            bottomStart = 10.dp
        ),
        modifier = Modifier.fillMaxWidth().height(80.dp).padding(top = 2.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black,
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 20.dp,
            pressedElevation = 10.dp,
        ),
        contentPadding = PaddingValues(4.dp)

    ) {
        Text(
            text= savedLists.owner,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f),
            maxLines = 2
        )
    }
}

@Composable
fun OwnerUnSelected(savedLists: SavedLists, savedListViewModel: SavedListViewModel) {
    Button(
        onClick = {
            savedListViewModel.setSelectedOwner(
                Pair(savedLists.savedListId, savedLists.owner)
            )
        },
        shape = RectangleShape,
        modifier = Modifier.height(80.dp).padding(top=2.dp, end = 2.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        contentPadding = PaddingValues(4.dp)
    ) {
        Text(
            text= savedLists.owner,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f),
            maxLines = 2
        )
    }
}
// endregion

//region Items of Owner UI
@Composable
fun ItemsOfSavedLists(
    savedListViewModel: SavedListViewModel,
    onEditIconButtonClick: (savedItemId: Int, savedItemLabel: String, savedItemDesc: String, currentStatus: String, ownerId: Int) -> Unit,
    modifier: Modifier
) {
    //CURT - revisit this logic with maybe a launcheffected higher up
    val savedListUIState by savedListViewModel.uiState.collectAsState()
    savedListViewModel.getAllSavedItemsForSelectedOwner()

    val savedLists = savedListUIState.allSavedItemsForList
    if (savedListUIState.showHintScreenToAddItems) {
        HintToAddItemsToOwner(modifier = modifier)
    } else {
        val ownerId = savedListViewModel.uiState.collectAsState().value.selectedOwner.first
        ContainerOfListItems(ownerId,savedLists, onEditIconButtonClick, modifier, savedListViewModel)
    }
}

@Composable
fun ContainerOfListItems(
    ownerId: Int,
    savedListItems: List<SavedItems>,
    onEditIconButtonClick: (savedItemId: Int, savedItemLabel: String, savedItemDesc: String, currentStatus: String, ownerId: Int) -> Unit,
    modifier: Modifier,
    savedListViewModel: SavedListViewModel
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        items(savedListItems)  {
            ElevatedCard(
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(8.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth().background(Color.LightGray)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,

                    ) {
                        Text(
                            text = it.label,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                        )
                        IconButton(onClick = {
                            onEditIconButtonClick(it.savedItemId, it.label, it.description, it.status,ownerId)
                        }, modifier = Modifier.padding(end = 8.dp)) {
                            Icon(
                                painterResource(R.drawable.edit_note_48),
                                contentDescription = "",
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                    Text(
                        text = it.description,
                        minLines = 2,
                        maxLines = 4,
                        modifier = Modifier
                            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp)) //CURT - must come before background
                            .background(Color.White)
                            .padding(8.dp) //CURT - padding for inside the shape... weird
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.Black,
                        modifier = Modifier.padding(
                            start = 4.dp,
                            top = 8.dp,
                            bottom = 8.dp,
                            end = 4.dp
                        )
                    )
                    StatusConfigurationForItem(it, savedListViewModel)
                }
            }
        }
    }
}

@Composable
fun StatusConfigurationForItem(savedItem: SavedItems, savedListViewModel: SavedListViewModel) {
    if (savedItem.statusType == StatusType.MenuStatusType) {
        Text(
            text = stringResource(R.string.status) + " : " + savedItem.status,
            textAlign = TextAlign.Right,
            modifier = Modifier.fillMaxWidth().padding(end = 8.dp)
        )
    } else {
        var localIsChecked by remember { mutableStateOf(savedItem.isChecked) }
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
        ) {
            Text(
                text = savedItem.status,
                modifier = Modifier.padding(8.dp)
            )
            Checkbox(
                checked = localIsChecked,
                onCheckedChange = { checked ->
                    localIsChecked = checked
                    val updatedSavedItem = savedItem.copy(
                        isChecked = checked
                    )
                    savedListViewModel.updateSavedItemForCheckboxOnly(updatedSavedItem)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Black,
                    uncheckedColor = Color.Black,
                    checkmarkColor = Color.White,
                )
            )
        }
    }
}

@Composable
fun HintToAddItemsToOwner(modifier: Modifier = Modifier) {
    Column (
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.listitem_add_48),
            stringResource(R.string.icon_add_listitem_cdescript),
            modifier = Modifier.size(65.dp).padding(bottom = 8.dp)
        )
        Text(
            text = stringResource(
                R.string.add_items_hint
            ),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
//endregion

// region Bottom Button UI
@Composable
fun BottomButtonEditRow(
    savedListViewModel: SavedListViewModel,
    modifier: Modifier = Modifier,
    onButtonClick: (Int) -> Unit
) {
    val savedListUIState by savedListViewModel.uiState.collectAsState()
    val selectedOwnerId = savedListUIState.selectedOwner.first

    Row(
        modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomIconButton(
            R.drawable.add_box_48,
            R.string.add_owner_button_cdescript) { savedListViewModel.setShowAddOwner(true) }
        BottomIconButton(
            R.drawable.listitem_add_48,
            R.string.icon_add_listitem_button_cdescript) {

           //CURT -  put check here to get the status type

            onButtonClick(selectedOwnerId)
        }
        BottomIconButton(
            R.drawable.delete_box_owner_48,
            R.string.icon_delete_owner_button_cdescript) {
            if (savedListUIState.allSavedLists.size > 1) {
                savedListViewModel.setShowDeletionOwner(true)
            } else
                savedListViewModel.setShowDeletionOfLastOwnerWarning(true)
        }
    }
}

@Composable
fun BottomIconButton(
    @DrawableRes drawableId: Int,
    @StringRes desc: Int,
    onIconClick: () -> Unit
) {
    IconButton(onClick = onIconClick) {
        Icon(
            painter = painterResource(drawableId),
            modifier = Modifier.size(65.dp),
            contentDescription = stringResource(desc)
        )
    }
}
// endregion

//region Operations Dialogs
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOwnerDialog(savedListViewModel: SavedListViewModel, categoryId: Int) {
    if (savedListViewModel.uiState.collectAsState().value.showAddOwnerDialog) {
        val collectedUiState = savedListViewModel.uiState.collectAsState().value

        BasicAlertDialog(
            onDismissRequest = { savedListViewModel.setShowAddOwner(false)}
        ) {
            Card {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.add_owner_48),
                        modifier = Modifier.size(48.dp),
                        contentDescription = stringResource(R.string.icon_add_listitem_cdescript)
                    )
                    Text(
                        text = stringResource(R.string.add_owner_message),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    CommonFormTextField(
                        label =R.string.new_owner,
                        textFieldState = savedListViewModel.addOwnerTextFieldState,
                        isError = collectedUiState.hasSQLError,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    ErrorMessageWithinDialog(collectedUiState)

                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CommonButton(
                            onClick = {
                                savedListViewModel.setShowAddOwner(false)
                                savedListViewModel.clearErrors()
                                savedListViewModel.clearTextFieldState()
                            },
                            text = stringResource(R.string.cancel)
                        )
                        CommonButton(
                            onClick = {
                                savedListViewModel.addOwner(
                                    categoryId,
                                    savedListViewModel.addOwnerTextFieldState.text.toString())
                            },
                            text = stringResource(R.string.add_owner)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteOwnerDialog(ownerToDelete: Pair<Int, String>, savedListViewModel: SavedListViewModel) {
    if (savedListViewModel.uiState.collectAsState().value.showDeletionDialog) {
        BasicAlertDialog(
            onDismissRequest = { savedListViewModel.setShowDeletionOwner(false) }
        ) {
            Card {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painterResource(R.drawable.list_remove_48),
                        modifier = Modifier.size(48.dp),
                        contentDescription = stringResource(R.string.icon_delete_owner_cdescript)
                    )
                    Text(
                        text = stringResource(
                            R.string.delete_owner_message,
                            ownerToDelete.second
                        ),
                        modifier = Modifier.padding(bottom = 8.dp),
                        textAlign = TextAlign.Center
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        CommonButton(
                            onClick = { savedListViewModel.setShowDeletionOwner(false) },
                            text = stringResource(R.string.cancel)
                        )
                        CommonButton(
                            onClick = { savedListViewModel.deleteOwner(ownerToDelete.first) },
                            text = stringResource(R.string.delete)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WarningForDeletingLastOwnerDialog(savedListViewModel: SavedListViewModel) {
    if (savedListViewModel.uiState.collectAsState().value.showDeletionOfLastOwnerDialog) {
        BasicAlertDialog(
            onDismissRequest = {savedListViewModel.setShowDeletionOfLastOwnerWarning(false)}
        ) {
            Card {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painterResource(R.drawable.list_remove_48),
                        modifier = Modifier.size(48.dp),
                        contentDescription = stringResource(R.string.icon_delete_owner_cdescript)
                    )
                    Text(
                        text = stringResource(
                            R.string.delete_last_owner_message
                        ),
                        modifier = Modifier.padding(bottom = 8.dp),
                        textAlign = TextAlign.Center
                    )
                    CommonButton(
                        onClick = { savedListViewModel.setShowDeletionOfLastOwnerWarning(false) },
                        text = stringResource(R.string.ok)
                    )
                }
            }
        }
    }
}

@Composable
fun ErrorMessageWithinDialog(savedListUiState: SavedListUIState) {
    val errorMsg = when {
        savedListUiState.hasSQLError -> stringResource(R.string.error_duplicate_owner)
        savedListUiState.hasBlankError -> stringResource(R.string.error_message_no_owner)
        else -> {""}
    }

    if (errorMsg.isNotEmpty()) {
        Text(
            text = errorMsg,
            color = Color.Red)
    }
}
//endregion
