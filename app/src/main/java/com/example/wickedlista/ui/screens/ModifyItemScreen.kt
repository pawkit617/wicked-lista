package com.example.wickedlista.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import com.example.wickedlista.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.wickedlista.CommonButton
import com.example.wickedlista.CommonFormTextField
import com.example.wickedlista.data.AddItemUIState
import com.example.wickedlista.ui.viewmodels.ModifyItemViewModel

@Composable
fun AddItemScreen(
    ownerId: Int,
    isAddingMore: Boolean = false,
    onDoneAddingItems: () -> Unit = {},
    modifyItemViewModel: ModifyItemViewModel = hiltViewModel()
) {
    SuccessAddMoreDialog(modifyItemViewModel, onDoneAddingItems)
    AddItemForm(modifyItemViewModel, ownerId, isAddingMore)
}

@Composable
fun AddItemForm(modifyItemViewModel: ModifyItemViewModel, ownerId: Int, isAddingMore: Boolean) {
    val addItemUIState by modifyItemViewModel.uiState.collectAsState()
    val useMenuForStatus = isAddingMore || addItemUIState.useMenuForStatus
    if (isAddingMore) {
        modifyItemViewModel.updateStatusesForItem(ownerId)
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().background(Color.White).align(Alignment.TopCenter),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            ItemInfo(modifyItemViewModel)
            ErrorMessageDisplayWithinCard(addItemUIState)
            HelpMessageForStatus(useMenuForStatus )//useMenuForStatus isAddingMore
            ItemStatuses(modifyItemViewModel, useMenuForStatus)//isAddingMore
            ItemStatusAsCheckboxSwitch(modifyItemViewModel)
        }
        Button(
            onClick = {modifyItemViewModel.addItemToListWithId(ownerId, useMenuForStatus)},
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier.fillMaxWidth().padding(8.dp).align(Alignment.BottomCenter)
        ) {
            Text(
                text = stringResource(R.string.create),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun EditItemScreen(
    savedItemId: Int,
    savedItemLabel: String,
    savedItemDesc: String,
    currentStatus: String,
    ownerId: Int,
    onDoneEditingItems: () -> Unit = {},
    modifyItemViewModel: ModifyItemViewModel = hiltViewModel()
) {
    val modifyItemUIState by modifyItemViewModel.uiState.collectAsState()
    modifyItemViewModel.prepopulateFormWithItemInfo(ownerId, savedItemLabel, savedItemDesc, currentStatus)

    Box(Modifier.fillMaxSize()){
        Column(
            modifier = Modifier.fillMaxWidth().background(Color.White),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ItemInfo(modifyItemViewModel)
            ErrorMessageDisplayWithinCard(modifyItemUIState)
            HelpMessageForStatus(true)
            ItemStatuses(modifyItemViewModel, true)
        }
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment =Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, bottom = 16.dp).align(Alignment.BottomCenter)
        ) {
            Button(
                onClick = { onDoneEditingItems() },
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    textAlign = TextAlign.Center,
                )
            }
            Button(
                onClick = {
                    modifyItemViewModel.deleteSavedItem(savedItemId)
                    onDoneEditingItems()
                },
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(text = stringResource(R.string.delete), textAlign = TextAlign.Center)
            }
            Button(
                onClick = {
                    val status = modifyItemViewModel.updateSavedItem(
                        savedItemId,
                        ownerId
                    )
                    if (status) {
                        onDoneEditingItems()
                    }
                },
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(text = stringResource(R.string.update), textAlign = TextAlign.Center)
            }
        }
    }

}
//region Modify Item Shared Form

@Composable
fun ItemInfo(modifyItemViewModel: ModifyItemViewModel) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().background(Color.LightGray)
        ) {
            CommonFormTextField(
                R.string.add_item_label,
                R.string.add_item_hint,
                modifyItemViewModel.labelTextFieldState,
                Modifier.fillMaxWidth().padding(8.dp)
            )
            CommonFormTextField(
                R.string.add_item_description_label,
                R.string.add_item_description_hint,
                modifyItemViewModel.descTextFieldState,
                Modifier.fillMaxWidth().padding(8.dp),
                lineLimits = TextFieldLineLimits.MultiLine(4)
            )
        }
    }
}

@Composable
fun HelpMessageForStatus(useMenu: Boolean = false) {
    val helpMessage = if (useMenu)
        R.string.add_item_status_choose_message
    else
        R.string.add_item_status_help_message

    Text(
        text = stringResource(helpMessage),
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}
@Composable
fun ItemStatuses(modifyItemViewModel: ModifyItemViewModel, useMenu: Boolean = false) {
    if (!modifyItemViewModel.uiState.collectAsState().value.useCheckboxStatus) {
        Card(modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().background(Color.LightGray)
            ) {
                if (useMenu) {
                    StatusAsMenu(modifyItemViewModel)
                } else {
                    StatusAsFormFields(modifyItemViewModel)
                }
            }
        }
    }
}

@Composable
fun ItemStatusAsCheckboxSwitch(modifyItemViewModel: ModifyItemViewModel) {
    Card(modifier = Modifier
        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 0.dp)
            ) {
                Text(text = stringResource(R.string.add_item_checkbox_toggle_message))
                Switch(
                    checked = modifyItemViewModel.uiState.collectAsState().value.useCheckboxStatus,
                    onCheckedChange = {
                        modifyItemViewModel.setUseCheckboxStatus(it)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color.Black,
                        uncheckedThumbColor = Color.Black,
                        uncheckedTrackColor = Color.White
                    )
                )
            }
            if (modifyItemViewModel.uiState.collectAsState().value.useCheckboxStatus) {
                CommonFormTextField(
                    R.string.add_item_checkbox_label,
                    R.string.done,
                    modifyItemViewModel.checkboxTextFieldState,
                    Modifier.fillMaxWidth().padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun StatusAsFormFields(modifyItemViewModel: ModifyItemViewModel) {
    CommonFormTextField(
        R.string.add_item_initial_status_label,
        R.string.add_item_initial_status_hint,
        modifyItemViewModel.initialStatusTextFieldState,
        Modifier.fillMaxWidth().padding(8.dp)
    )
    CommonFormTextField(
        R.string.add_item_additional_status_label,
        R.string.add_item_additional_status_hint,
        modifyItemViewModel.additionalStatusTextFieldState,
        Modifier.fillMaxWidth().padding(8.dp)
    )
    CommonFormTextField(
        R.string.add_item_additional_status_label,
        R.string.add_item_additional_status_hint2,
        modifyItemViewModel.additionalStatus2TextFieldState,
        Modifier.fillMaxWidth().padding(8.dp)
    )
    CommonFormTextField(
        R.string.add_item_additional_status_label,
        R.string.add_item_additional_status_hint3,
        modifyItemViewModel.additionalStatus3TextFieldState,
        Modifier.fillMaxWidth().padding(8.dp)
    )
}

@Composable
fun FormTextField(
    @StringRes label: Int,
    @StringRes hint: Int,
    textFieldState: TextFieldState,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    ) {
    OutlinedTextField(
        label = { Text(text = stringResource(label)) },
        placeholder = {Text(text = stringResource(hint)) },
        state = textFieldState,
        modifier = modifier,
        lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = maxLines),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

@Composable
fun StatusAsMenu(modifyItemViewModel: ModifyItemViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val iconForTextField = if (expanded) R.drawable.arrow_drop_up_48 else R.drawable.arrow_drop_down_48
    val stateOfTextField = modifyItemViewModel.statusTextFieldForMenuState

    OutlinedTextField(
        label = { Text(text = stringResource(R.string.status)) },
        placeholder = { Text(text = stringResource(R.string.select_status_hint)) },
        state = stateOfTextField,
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        readOnly = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        trailingIcon = {
            Icon(
                painterResource(iconForTextField),
                modifier = Modifier.clickable { expanded = !expanded },
                contentDescription = stringResource(R.string.status_menu_drop_down_icon)
            )
        }
    )
    DropdownMenu(
        expanded = expanded,
        modifier = Modifier.background(Color.White),
        onDismissRequest = { expanded = false }
    ) {
        val createdStatuses = modifyItemViewModel.uiState.collectAsState().value.itemStatuses

        createdStatuses.forEach {
            val nameOfStatus = it
            DropdownMenuItem(
                { Text(text = nameOfStatus, modifier = Modifier.background(Color.White)) },
                onClick = {
                    expanded = false
                    stateOfTextField.edit {
                        replace(0, stateOfTextField.text.length, nameOfStatus)
                    }
                }
            )
        }
    }
}
//endregion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessAddMoreDialog(modifyItemViewModel: ModifyItemViewModel, onDoneAction: () ->Unit) {
    if (modifyItemViewModel.uiState.collectAsState().value.showSuccessAddMoreItemDialog) {
        BasicAlertDialog(
            onDismissRequest = { modifyItemViewModel.setShowSuccessAddMoreItemDialog(false) }
        ) {
            Card {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(R.drawable.success_check_48),
                        contentDescription = stringResource(R.string.add_item_success_message_icon),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = stringResource(R.string.add_item_successful_add_message),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CommonButton(
                            onClick = {
                                //dialog with label, desc, and spinner
                                modifyItemViewModel.setShowSuccessAddMoreItemDialog(false)
                                modifyItemViewModel.setUseMenuForStatus(true)
                            },
                            text = stringResource(R.string.add_more)
                        )
                        CommonButton(
                            onClick = {
                                modifyItemViewModel.setShowSuccessAddMoreItemDialog(false)
                                onDoneAction()
                            },
                            text = stringResource(R.string.done)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorMessageDisplayWithinCard(addItemUIState: AddItemUIState) {
    val errMsg = when {
        addItemUIState.hasBlankLabelError -> R.string.error_add_item_blank_label
        addItemUIState.hasBlankStatusError -> R.string.error_add_item_blank_status
        else -> -1
    }

    if (errMsg != -1) {
        Text(
            text = stringResource(errMsg),
            color = Color.Red,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center)
    }
}