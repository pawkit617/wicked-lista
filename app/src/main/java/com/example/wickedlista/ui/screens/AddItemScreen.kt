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
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.wickedlista.ui.viewmodels.AddItemViewModel

@Composable
fun AddItemScreen(
    ownerId: Int,
    onDoneAddingItems: () -> Unit = {},
    addItemViewModel: AddItemViewModel = hiltViewModel()
) {
    SuccessAddMoreDialog(ownerId, addItemViewModel, onDoneAddingItems)
    AddItemForm(addItemViewModel, ownerId)
}
//region Add Item Form
@Composable
fun AddItemForm(addItemViewModel: AddItemViewModel, ownerId: Int) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().background(Color.White).align(Alignment.TopCenter),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ItemInfo(addItemViewModel)
            ItemStatuses(addItemViewModel)
        }

        Button(
            onClick = {addItemViewModel.addItemToList(ownerId)},
            shape = MaterialTheme.shapes.large,
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
fun ItemInfo(addItemViewModel: AddItemViewModel) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().background(Color.LightGray)
        ) {
            FormTextField(
                R.string.add_item_label,
                R.string.add_items_hint,
                addItemViewModel.labelTextFieldState,
                Modifier.fillMaxWidth().padding(8.dp)
            )
            FormTextField(
                R.string.add_item_description_label,
                R.string.add_item_description_hint,
                addItemViewModel.descTextFieldState,
                Modifier.fillMaxWidth().padding(8.dp),
                4
            )
        }
    }
}

@Composable
fun ItemStatuses(addItemViewModel: AddItemViewModel) {
    val addItemViewModelStatus by addItemViewModel.uiState.collectAsState()

    val helpMessage = if (!addItemViewModelStatus.showAdditionalItemsDialog)
        R.string.add_item_status_help_message
    else
        R.string.add_item_status_choose_message

    Text(
        text = stringResource(helpMessage),
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        textAlign = TextAlign.Center
    )

    Card(modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().background(Color.LightGray)
        ) {
            if (addItemViewModelStatus.showAdditionalItemsDialog) {
                StatusAsMenu(addItemViewModel)
            } else {
                StatusAsFormFields(addItemViewModel)
            }
        }
    }
}

@Composable
fun StatusAsFormFields(addItemViewModel: AddItemViewModel) {
    FormTextField(
        R.string.add_item_initial_status_label,
        R.string.add_item_initial_status_hint,
        addItemViewModel.initialStatusTextFieldState,
        Modifier.fillMaxWidth().padding(8.dp)
    )
    FormTextField(
        R.string.add_item_additional_status_label,
        R.string.add_item_additional_status_hint,
        addItemViewModel.additionalStatusTextFieldState,
        Modifier.fillMaxWidth().padding(8.dp)
    )
    FormTextField(
        R.string.add_item_additional_status_label,
        R.string.add_item_additional_status_hint2,
        addItemViewModel.additionalStatus2TextFieldState,
        Modifier.fillMaxWidth().padding(8.dp)
    )
    FormTextField(
        R.string.add_item_additional_status_label,
        R.string.add_item_additional_status_hint3,
        addItemViewModel.additionalStatus3TextFieldState,
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
        placeholder = { Text(text = stringResource(hint)) },
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
fun StatusAsMenu(addItemViewModel: AddItemViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val iconForTextField = if (expanded) R.drawable.arrow_drop_up_48 else R.drawable.arrow_drop_down_48
    val stateOfTextField = addItemViewModel.statusTextFieldForMenuState

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
                contentDescription = ""
            )
        }
    )
    DropdownMenu(
        expanded = expanded,
        modifier = Modifier.background(Color.White),
        onDismissRequest = {}
    ) {
        val createdStatuses = addItemViewModel.uiState.collectAsState().value.itemStatuses

        createdStatuses.forEach { it ->
            val nameOfStatus = it.toString()
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
fun SuccessAddMoreDialog(ownerId: Int, addItemViewModel: AddItemViewModel, onDoneAction: () ->Unit) {
    if (addItemViewModel.uiState.collectAsState().value.showSuccessAddAMoreItemDialog) {
        BasicAlertDialog(
            onDismissRequest = { addItemViewModel.setShowSuccessAddAMoreItemDialog(false) }
        ) {
            Card {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(R.drawable.success_check_48),
                        contentDescription = "icon add success",
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
                        Button(
                            onClick = {
                                //dialog with label, desc, and spinner
                                addItemViewModel.setShowSuccessAddAMoreItemDialog(false)
                                addItemViewModel.setAdditionalItemsDialog(true)
                            },
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = stringResource(R.string.add_more),
                                textAlign = TextAlign.Center
                            )
                        }
                        Button(
                            onClick = {
                                addItemViewModel.setShowSuccessAddAMoreItemDialog(false)
                                onDoneAction()
                            },
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = stringResource(R.string.done),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewScreen(id: Int = 1) {
    //AdditionalItemsDialog(1)
    //AddItemScreen(id)
   //SuccessAddMoreDialog()
}