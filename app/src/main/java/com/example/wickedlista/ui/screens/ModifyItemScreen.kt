package com.example.wickedlista.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.wickedlista.StyleButton
import com.example.wickedlista.CommonFormTextField
import com.example.wickedlista.R
import com.example.wickedlista.data.AddItemUIState
import com.example.wickedlista.database.saveditems.StatusType
import com.example.wickedlista.ui.theme.GetBoxAsDropShadow
import com.example.wickedlista.ui.theme.getCardForModifyItemFormStyle
import com.example.wickedlista.ui.theme.getDialogStyle
import com.example.wickedlista.ui.viewmodels.ModifyItemViewModel

@Composable
fun AddItemScreen(
    ownerId: Int,
    onDoneAddingItems: () -> Unit = {},
    modifyItemViewModel: ModifyItemViewModel = hiltViewModel()
) {
    SuccessAddMoreDialog(modifyItemViewModel, onDoneAddingItems)
    AddItemForm(modifyItemViewModel, ownerId)
}

@Composable
fun AddItemForm(modifyItemViewModel: ModifyItemViewModel, ownerId: Int) {
    val addItemUIState by modifyItemViewModel.uiState.collectAsState()
    LaunchedEffect(key1 = ownerId) {
        modifyItemViewModel.checkItemsStatusesForOwnerId(ownerId)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(0.9f)
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp, end = 8.dp)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.surfaceContainerLowest),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ItemInfo(modifyItemViewModel)
            ItemStatusAsCheckboxSwitch(modifyItemViewModel)
            ErrorMessageDisplayWithinCard(addItemUIState)
            HelpMessageForStatus(addItemUIState)
            ItemStatuses(modifyItemViewModel)
        }
        GetBoxAsDropShadow()
        Column (
            modifier = Modifier
                .weight(0.1f)
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            StyleButton(
                onClick = {
                    if (addItemUIState.useCheckboxStatus) {
                        modifyItemViewModel.addItemToListWithIdForCheckedLabel(
                            ownerId,
                        )
                    } else {
                        modifyItemViewModel.addItemToListWithId(ownerId, addItemUIState.useMenuForStatus)
                    }
                },
                text = stringResource(R.string.create),
                contentColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                backgroundColor = MaterialTheme.colorScheme.tertiary,
                buttonWidth = 250.dp
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
    LaunchedEffect(key1 = ownerId) {
        modifyItemViewModel.prepopulateFormWithItemInfo(ownerId, savedItemLabel, savedItemDesc, currentStatus)
    }

    val modifyItemUIState by modifyItemViewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(0.9f)
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp, end = 8.dp)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.surfaceContainerLowest),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ItemInfo(modifyItemViewModel)
            ErrorMessageDisplayWithinCard(modifyItemUIState)
            HelpMessageForStatus(modifyItemViewModel.uiState.collectAsState().value)
            ItemStatuses(modifyItemViewModel)
        }
        GetBoxAsDropShadow()
        Column(
            modifier = Modifier
                .weight(0.1f)
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                StyleButton(
                    {
                        modifyItemViewModel.deleteSavedItem(savedItemId)
                        onDoneEditingItems()
                    },
                    text = stringResource(R.string.delete),
                    backgroundColor = MaterialTheme.colorScheme.onErrorContainer,
                    contentColor = MaterialTheme.colorScheme.surfaceDim,
                    pressedColor = MaterialTheme.colorScheme.error
                )
                StyleButton(
                    onClick = { onDoneEditingItems() },
                    text = stringResource(R.string.cancel),
                )
                val statusType = modifyItemViewModel.uiState.collectAsState().value.statusType
                StyleButton(
                    onClick = {
                        val status = modifyItemViewModel.updateSavedItem(
                            savedItemId,
                            ownerId,
                            statusType
                        )
                        if (status) {
                            onDoneEditingItems()
                        }
                    },
                    text = stringResource(R.string.update),
                    contentColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    backgroundColor = MaterialTheme.colorScheme.tertiary,
                )
            }
        }
    }

}
//region Modify Item Shared Form

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun ItemInfo(modifyItemViewModel: ModifyItemViewModel) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier.styleable(
        null,
        getCardForModifyItemFormStyle(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            myShape = MaterialTheme.shapes.medium))
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 16.dp, 8.dp, 16.dp)

        ) {
            CommonFormTextField(
                R.string.add_item_label,
                modifyItemViewModel.labelTextFieldState
            )
            CommonFormTextField(
                R.string.add_item_description_label,
                modifyItemViewModel.descTextFieldState,
                lineLimits = TextFieldLineLimits.MultiLine(4),
                insertSpacer = false
            )
        }
    }
}

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun ItemStatusAsCheckboxSwitch(modifyItemViewModel: ModifyItemViewModel) {
    val addItemUiState = modifyItemViewModel.uiState.collectAsState().value
    if (addItemUiState.statusType.type != StatusType.MenuStatusType.type) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            modifier = Modifier.styleable(
                null,
                getCardForModifyItemFormStyle(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    myShape = MaterialTheme.shapes.medium,
                    topExternalPadding = 16.dp))
        ){
            if (addItemUiState.statusType.type == StatusType.CheckboxStatusType.type) {
                ItemStatusAsCheckbox(modifyItemViewModel)
            } else {
                ItemStatusConfigWithLabelAndSwitch(modifyItemViewModel)
            }
        }
    }
}

@Composable
fun ItemStatusConfigWithLabelAndSwitch(modifyItemViewModel: ModifyItemViewModel) {
    val localUiState = modifyItemViewModel.uiState.collectAsState().value
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val contentDescForSwitch = stringResource(R.string.switch_for_checkbox_cdescript)
            Text(
                text = stringResource(R.string.add_item_checkbox_toggle_message),
                modifier = Modifier.padding(end = 8.dp)
            )
            Switch(
                checked = localUiState.useCheckboxStatus,
                onCheckedChange = {
                    modifyItemViewModel.setUseCheckboxStatus(it)
                    if (it) {
                        modifyItemViewModel.clearTextFieldStatesForStatusMenu()
                        modifyItemViewModel.clearErrors()
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MaterialTheme.colorScheme.tertiary,
                    uncheckedThumbColor = Color.Black,
                    uncheckedTrackColor = Color.White
                ),
                modifier = Modifier.semantics { contentDescription = contentDescForSwitch }
            )
        }
        if (localUiState.useCheckboxStatus) {
            CommonFormTextField(
                R.string.add_item_checkbox_label,
                modifyItemViewModel.checkboxTextFieldState,
            )
        }
    }
}

@Composable
fun ItemStatusAsCheckbox(modifyItemViewModel: ModifyItemViewModel) {
    val localUiState = modifyItemViewModel.uiState.collectAsState().value
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = localUiState.itemStatusCheckboxLabel,
            modifier = Modifier.padding(8.dp)
        )
        val checkboxContentDesc = stringResource(R.string.status_checkbox_cdescript)
        Checkbox(
            checked = localUiState.itemStatusCheckboxChecked,
            onCheckedChange = { checked ->
                modifyItemViewModel.setItemStatusCheckboxChecked(checked)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.tertiary,
                uncheckedColor = Color.Black,
                checkmarkColor = Color.White,
            ),
            modifier = Modifier.semantics { contentDescription = checkboxContentDesc }
        )
    }
}

@Composable
fun HelpMessageForStatus(addItemUIState: AddItemUIState) {
    if (!addItemUIState.useCheckboxStatus) {

        val helpMessage = if (addItemUIState.statusType.type == StatusType.MenuStatusType.type)
            R.string.add_item_status_choose_message
        else
            R.string.add_item_status_help_message

        Text(
            text = stringResource(helpMessage),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}
@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun ItemStatuses(modifyItemViewModel: ModifyItemViewModel) {
    val addItemUIState = modifyItemViewModel.uiState.collectAsState().value
    if (!addItemUIState.useCheckboxStatus) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            modifier = Modifier.styleable(
                null,
                getCardForModifyItemFormStyle(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    myShape = MaterialTheme.shapes.medium)
            )
        ){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 16.dp, 8.dp, 16.dp)
            ) {
                if (addItemUIState.statusType.type == StatusType.MenuStatusType.type) {
                    StatusAsMenu(modifyItemViewModel)
                } else {
                    StatusAsFormFields(modifyItemViewModel)
                }
            }
        }
    }
}

@Composable
fun StatusAsFormFields(modifyItemViewModel: ModifyItemViewModel) {
    CommonFormTextField(
        R.string.add_item_initial_status_label,
        modifyItemViewModel.initialStatusTextFieldState,
    )
    CommonFormTextField(
        R.string.add_item_additional_status_label,
        modifyItemViewModel.additionalStatusTextFieldState,
    )
    CommonFormTextField(
        R.string.add_item_additional_status_label,
        modifyItemViewModel.additionalStatus2TextFieldState,
    )
    CommonFormTextField(
        R.string.add_item_additional_status_label,
        modifyItemViewModel.additionalStatus3TextFieldState,
        insertSpacer = false
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        readOnly = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        trailingIcon = {
            Icon(
                painterResource(iconForTextField),
                modifier = Modifier.clickable { expanded = !expanded },
                contentDescription = stringResource(R.string.status_menu_drop_down_icon),
                tint = MaterialTheme.colorScheme.secondary
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationStyleApi::class)
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
                    modifier = Modifier
                        .styleable(null, getDialogStyle(MaterialTheme.colorScheme.surfaceDim))
                ) {
                    Image(
                        painter = painterResource(R.drawable.success_check_48),
                        contentDescription = stringResource(R.string.add_item_success_message_icon),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = stringResource(R.string.add_item_successful_add_message),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        StyleButton(
                            onClick = {
                                //dialog with label, desc, and spinner
                                modifyItemViewModel.setShowSuccessAddMoreItemDialog(false)

                            },
                            text = stringResource(R.string.add_more)
                        )
                        StyleButton(
                            onClick = {
                                modifyItemViewModel.setShowSuccessAddMoreItemDialog(false)
                                onDoneAction()
                            },
                            text = stringResource(R.string.done),
                            backgroundColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.surfaceContainerLowest,
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