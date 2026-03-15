package com.example.wickedlista.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import com.example.wickedlista.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    addItemViewModel: AddItemViewModel = hiltViewModel()
) {
    //val addItemViewModelState by addItemViewModel.uiState.collectAsState()
    SuccessAddMoreDialog(ownerId, addItemViewModel)
    AddItemForm(addItemViewModel, ownerId)
}
//region Add Item Form
@Composable
fun AddItemForm(addItemViewModel: AddItemViewModel, ownerId: Int) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        ItemInfo(addItemViewModel)
        ItemStatuses(addItemViewModel)
        Button(
            onClick = {addItemViewModel.addItemToList(ownerId)},
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
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
                Modifier.fillMaxWidth().padding(8.dp)
            )
        }
    }
}

@Composable
fun ItemStatuses(addItemViewModel: AddItemViewModel) {
    Text(
        text = stringResource(R.string.add_item_status_help_message),
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        textAlign = TextAlign.Center
    )

    Card(modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().background(Color.LightGray)
        ) {
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
    }
}

@Composable
fun FormTextField(
    @StringRes label: Int,
    @StringRes hint: Int,
    textFieldState: TextFieldState,
    modifier: Modifier = Modifier) {
    OutlinedTextField(
        label = { Text(text = stringResource(label)) },
        placeholder = { Text(text = stringResource(hint)) },
        state = textFieldState,
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}
//endregion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessAddMoreDialog(ownerId: Int, addItemViewModel: AddItemViewModel) {
    if (addItemViewModel.uiState.collectAsState().value.showSuccessAddAMoreItemDialog) {
        BasicAlertDialog(
            onDismissRequest = { addItemViewModel.setShowSuccessAddAMoreItemDialog(false) }
        ) {
            Card {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
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
                                text = "Add more",
                                textAlign = TextAlign.Center
                            )
                        }
                        Button(
                            onClick = {
                                addItemViewModel.setShowSuccessAddAMoreItemDialog(false)
                                //nav back
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

@Composable
fun AdditionalItemsDialog(ownerId: Int, addItemViewModel: AddItemViewModel) {
    if (addItemViewModel.uiState.collectAsState().value.showAdditionalItemsDialog) {
        ItemInfo(addItemViewModel)
    }
}

@Preview
@Composable
fun PreviewScreen(id: Int = 1) {
    //AddItemScreen(id)
   //SuccessAddMoreDialog()
}