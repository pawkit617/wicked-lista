package com.example.wickedlista.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import com.example.wickedlista.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.wickedlista.ui.viewmodels.AddItemViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun AddItemScreen(
    ownerId: Int? = -1,
    modifier: Modifier = Modifier.fillMaxWidth(),
    addItemViewModel: AddItemViewModel = hiltViewModel()
) {
    Column(

        modifier = Modifier.fillMaxSize()
    ) {
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
                    addItemViewModel.initialStatusTextFieldState,
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
        Button(
            onClick = {},
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

@Preview
@Composable
fun preview() {
    AddItemScreen()
}