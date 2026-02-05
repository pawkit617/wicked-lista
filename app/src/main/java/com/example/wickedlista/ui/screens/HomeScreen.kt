package com.example.wickedlista.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wickedlista.R
import com.example.wickedlista.ui.viewmodels.HomeScreenViewModel


@Composable
fun HomeScreen(
    homeScreenViewModel: HomeScreenViewModel = viewModel(),
    createNewList: () -> Unit,
    modifier: Modifier = Modifier,
    contentPaddingValues: PaddingValues = PaddingValues(0.dp)
) { //cdc- will need a view model to show the lists or the no list msg


    val showCreateListDialog = rememberSaveable { mutableStateOf(false) }
    if (showCreateListDialog.value) {
        CreateListDialog(homeScreenViewModel) {showCreateListDialog.value = it}
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(contentPaddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text(
            modifier = Modifier.padding(0.dp, 0.dp,0.dp, 16.dp),
            text = stringResource(R.string.no_list_message),
            style = MaterialTheme.typography.titleLarge
        )
        Button(
            shape = MaterialTheme.shapes.small,
            onClick = {
                showCreateListDialog.value = true
            }
        ) {
            Text(
                text = stringResource(R.string.create_a_list),
                style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun CreateListDialog(homeScreenViewModel: HomeScreenViewModel, setShowDialog: (Boolean) -> Unit) {
    val homeScreenUIState = homeScreenViewModel.uiState.collectAsState()
    val titleState = homeScreenViewModel.titleSavedState
    val subjectState = homeScreenViewModel.subjectSavedState

    Dialog(
        onDismissRequest = {setShowDialog(false)}
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
                    label = {
                        Text(text = stringResource(R.string.title))
                    },
                    isError = homeScreenUIState.value.isError,
                    lineLimits = TextFieldLineLimits.SingleLine,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(Modifier.padding(8.dp))

                if (homeScreenUIState.value.isError) {
                    Text(
                        text = stringResource(R.string.error_message),
                        color = Color.Red)
                }

                OutlinedTextField(
                    state = subjectState,
                    label = {
                        Text(text = stringResource(R.string.subject))
                    },
                    lineLimits = TextFieldLineLimits.SingleLine,
                    placeholder = {Text(text = stringResource(R.string.optional))},
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.padding(8.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {setShowDialog(false)},
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    Button(
                        onClick = {
                            homeScreenViewModel.createNewList()
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

private fun validateInput(validator: (value: String, maxCount: Int, minCount: Int) -> Boolean): Boolean {
    return true
}