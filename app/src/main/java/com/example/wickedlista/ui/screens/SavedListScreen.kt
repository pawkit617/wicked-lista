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
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.styleable
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
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.wickedlista.CommonFormTextField
import com.example.wickedlista.R
import com.example.wickedlista.StyleButton
import com.example.wickedlista.data.SavedListUIState
import com.example.wickedlista.database.saveditems.SavedItems
import com.example.wickedlista.database.saveditems.StatusType
import com.example.wickedlista.database.savedlists.SavedLists
import com.example.wickedlista.ui.theme.GetBoxAsDropShadow
import com.example.wickedlista.ui.theme.commonIconButtonPressedShadow
import com.example.wickedlista.ui.theme.commonIconButtonShadow
import com.example.wickedlista.ui.theme.getCardListItemStyle
import com.example.wickedlista.ui.theme.getCommonTextFieldStyle
import com.example.wickedlista.ui.theme.getDialogStyle
import com.example.wickedlista.ui.viewmodels.SavedListViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

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
        Column(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
        ) {
            if (topicName.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.topic_subtitle) + topicName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .weight(0.9f)
            ) {
                OwnersOfSavedList(
                    listOfSavedListsOwners,
                    savedListViewModel,
                    Modifier
                        .weight(0.25f)
                        .background(color = MaterialTheme.colorScheme.secondary)
                )
                ItemsOfSavedLists(
                    savedListViewModel,
                    onEditIconButtonClick,
                    Modifier
                        .weight(0.75f)
                        .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
                )
            }
            GetBoxAsDropShadow()
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

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun ContainerOfListItems(
    ownerId: Int,
    savedListItems: List<SavedItems>,
    onEditIconButtonClick: (savedItemId: Int, savedItemLabel: String, savedItemDesc: String, currentStatus: String, ownerId: Int) -> Unit,
    modifier: Modifier,
    savedListViewModel: SavedListViewModel
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth().padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 0.dp)
    ) {
        items(savedListItems)  {
            Card(
                modifier = Modifier.styleable(null, getCardListItemStyle())
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.secondaryContainer,)
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
                                tint = MaterialTheme.colorScheme.tertiary,
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
                            .padding(8.dp)
                            .styleable(
                            null, 
                            getCommonTextFieldStyle(
                                shapeOfTextField = RoundedCornerShape(10.dp),
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                backgroundColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                                contentColor = Color.Black,
                                height = 75.dp
                            )
                        )
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
    if (savedItem.statusType == StatusType.MenuStatusType) { //Curt - on update it get StatusType.Unassigned and causes the defect
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
                    checkedColor = MaterialTheme.colorScheme.tertiary,
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
@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun BottomButtonEditRow(
    savedListViewModel: SavedListViewModel,
    modifier: Modifier = Modifier,
    onButtonClick: (Int) -> Unit
) {
    val savedListUIState by savedListViewModel.uiState.collectAsState()
    val selectedOwnerId = savedListUIState.selectedOwner.first

    Row(
        modifier.background(MaterialTheme.colorScheme.surface),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomIconButton(
            R.drawable.delete_box_owner_48,
            R.string.icon_delete_owner_button_cdescript,
            MaterialTheme.colorScheme.error) {
            if (savedListUIState.allSavedLists.size > 1) {
                savedListViewModel.setShowDeletionOwner(true)
            } else
                savedListViewModel.setShowDeletionOfLastOwnerWarning(true)
        }
        BottomIconButton(
            R.drawable.listitem_add_48,
            R.string.icon_add_listitem_button_cdescript) {
            onButtonClick(selectedOwnerId)
        }
        BottomIconButton(
            R.drawable.add_box_48,
            R.string.add_owner_button_cdescript) {
            savedListViewModel.setShowAddOwner(true)
        }
    }
}

@Composable
fun BottomIconButton(
    @DrawableRes drawableId: Int,
    @StringRes desc: Int,
    iconColor: Color = MaterialTheme.colorScheme.tertiary,
    onIconClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    if (isPressed) {
        LaunchedEffect(Unit) {
            delay(300.milliseconds)
            isPressed = false
        }
    }

    IconButton(
        onClick = {
            isPressed = true
            onIconClick()
        },
        shape = RectangleShape,
        modifier = Modifier.dropShadow(
            RectangleShape,
            if (!isPressed)commonIconButtonShadow else commonIconButtonPressedShadow
        )
    ) {
        Icon(
            painter = painterResource(drawableId),
            tint = iconColor,
            modifier = Modifier.size(65.dp).background(MaterialTheme.colorScheme.surface),
            contentDescription = stringResource(desc),

        )
    }
}
// endregion

//region Operations Dialogs
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationStyleApi::class)
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
                        .styleable(
                            null,
                            getDialogStyle(
                                MaterialTheme.colorScheme.surfaceDim
                            )
                        ),
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
                        hint = R.string.new_owner,
                        textFieldState = savedListViewModel.addOwnerTextFieldState,
                        isError = collectedUiState.hasSQLError
                    )
                    ErrorMessageWithinDialog(collectedUiState)

                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        StyleButton(
                            onClick = {
                                savedListViewModel.setShowAddOwner(false)
                                savedListViewModel.clearErrors()
                                savedListViewModel.clearTextFieldState()
                            },
                            text = stringResource(R.string.cancel)
                        )
                        StyleButton(
                            onClick = {
                                savedListViewModel.addOwner(
                                    categoryId,
                                    savedListViewModel.addOwnerTextFieldState.text.toString())
                            },
                            text = stringResource(R.string.add_owner),
                            contentColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            backgroundColor = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationStyleApi::class)
@Composable
fun DeleteOwnerDialog(ownerToDelete: Pair<Int, String>, savedListViewModel: SavedListViewModel) {
    if (savedListViewModel.uiState.collectAsState().value.showDeletionDialog) {
        BasicAlertDialog(
            onDismissRequest = { savedListViewModel.setShowDeletionOwner(false) }
        ) {
            Card {
                Column(
                    Modifier
                        .styleable(
                            null,
                            getDialogStyle(
                                MaterialTheme.colorScheme.surfaceDim
                            )
                        ),
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
                        StyleButton(
                            onClick = { savedListViewModel.setShowDeletionOwner(false) },
                            text = stringResource(R.string.cancel)
                        )
                        StyleButton(
                            onClick = { savedListViewModel.deleteOwner(ownerToDelete.first) },
                            text = stringResource(R.string.delete)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationStyleApi::class)
@Composable
fun WarningForDeletingLastOwnerDialog(savedListViewModel: SavedListViewModel) {
    if (savedListViewModel.uiState.collectAsState().value.showDeletionOfLastOwnerDialog) {
        BasicAlertDialog(
            onDismissRequest = {savedListViewModel.setShowDeletionOfLastOwnerWarning(false)}
        ) {
            Card {
                Column(
                    Modifier
                        .styleable(
                            null,
                            getDialogStyle(
                                MaterialTheme.colorScheme.surfaceDim
                            )
                        ),
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
                    StyleButton(
                        onClick = { savedListViewModel.setShowDeletionOfLastOwnerWarning(false) },
                        text = stringResource(R.string.ok),
                        contentColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                        backgroundColor = MaterialTheme.colorScheme.tertiary
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
