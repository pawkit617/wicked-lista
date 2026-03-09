package com.example.wickedlista.ui.screens

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.wickedlista.R
import com.example.wickedlista.data.SavedListUIState
import com.example.wickedlista.database.savedlists.SavedLists
import com.example.wickedlista.ui.viewmodels.SavedListViewModel

@Composable
fun SavedListScreen(
    categoryId: Int,
    savedListViewModel: SavedListViewModel = hiltViewModel()
) {
    val savedListViewModelState by savedListViewModel.uiState.collectAsState() //using 'by' allows u to avoid .value()
    LaunchedEffect(savedListViewModelState.allSavedLists.size) {
        savedListViewModel.getAllSavedListsForCategoryId(categoryId)
    }

    val listOfSavedListsOwners = savedListViewModelState.allSavedLists

    AddOwnerDialog(savedListViewModel, categoryId)
    OwnersWithListItems(listOfSavedListsOwners, savedListViewModel)

}

@Composable
fun OwnersWithListItems(
    listOfSavedListsOwners: List<SavedLists>,
    savedListViewModel: SavedListViewModel
) {
    if (listOfSavedListsOwners.isNotEmpty()) {
        Column(modifier = Modifier.fillMaxSize()) {
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
            )
        }
    }
}

@Composable
fun BottomButtonEditRow(
    savedListViewModel: SavedListViewModel,
    modifier: Modifier = Modifier
) {
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
            R.string.icon_add_listitem_cdescript) {}
        BottomIconButton(
            R.drawable.delete_box_owner_48,
            R.string.icon_delete_owner_cdescript) {}
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


@Composable
fun OwnersOfSavedList(savedLists: List<SavedLists>, savedListViewModel: SavedListViewModel, modifier: Modifier) {
    val currentlySelectedOwner = savedListViewModel.uiState.collectAsState().value.selectedOwnerId
    LazyColumn(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(savedLists) {
            if (it.savedListId == currentlySelectedOwner) {
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
        )

    ) {
        Text(
            text= savedLists.owner,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun OwnerUnSelected(savedLists: SavedLists, savedListViewModel: SavedListViewModel) {
    Button(
        onClick = {savedListViewModel.setSelectedOwnerId(savedLists.savedListId)},
        shape = RectangleShape,
        modifier = Modifier.fillMaxWidth().height(80.dp).padding(top=2.dp, end = 2.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
    ) {
        Text(
            text= savedLists.owner,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ItemsOfSavedLists(savedListViewModel: SavedListViewModel, modifier: Modifier) {
    val currentlySelectedOwner = savedListViewModel.uiState.collectAsState().value.selectedOwnerId
    HintToAddItemsToOwner("Hank", modifier = modifier)
//    LazyColumn(
//        modifier = modifier
//    ) {
//        items(savedLists) {
//            Text(text = "Here is where the listed items go, man.")
//        }
//
//    }
}

@Composable
fun HintToAddItemsToOwner(ownerName: String, modifier: Modifier) {
    Column (
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.listitem_add_48),
            "",
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
                    modifier = Modifier.padding(8.dp),
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
                    OutlinedTextField(
                        state = savedListViewModel.addOwnerTextFieldState,
                        label = { Text(text = stringResource(R.string.new_owner)) },
                        isError = collectedUiState.hasSQLError,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    ErrorMessageWithinDialog(collectedUiState)

                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                savedListViewModel.setShowAddOwner(false)
                                savedListViewModel.clearErrors()
                                savedListViewModel.clearTextFieldState()
                            },
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(text = stringResource(R.string.cancel))

                        }
                        Button(
                            onClick = {savedListViewModel.addOwner(
                                categoryId,
                                savedListViewModel.addOwnerTextFieldState.text.toString())
                            },
                            shape = MaterialTheme.shapes.small) {
                            Text(text = stringResource(R.string.add_owner))
                        }
                    }
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
