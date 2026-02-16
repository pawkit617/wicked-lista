package com.example.wickedlista.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.wickedlista.R
import com.example.wickedlista.data.HomeScreenUIState
import com.example.wickedlista.database.HomeLists
import com.example.wickedlista.ui.viewmodels.HomeScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun HomeScreen(
    homeScreenViewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier,
    contentPaddingValues: PaddingValues = PaddingValues(0.dp)
) {
    val showCreateListDialog = rememberSaveable { mutableStateOf(false) }
    val allListsAsState = homeScreenViewModel.getLists().collectAsState(emptyList())
    val allLists = allListsAsState.value
    if (homeScreenViewModel.uiState.collectAsState().value.showCreateDialog) {
        CreateListDialog(homeScreenViewModel)
    }
    else if (allLists.isEmpty()) {
        NoListFoundScreen(homeScreenViewModel, contentPaddingValues)
    } else {
        ListsScreen(allLists, contentPaddingValues)
    }

}

@Composable
fun ListsScreen(
    allLists: List<HomeLists>,
    contentPaddingValues: PaddingValues
) {

    LazyVerticalGrid(
        columns = GridCells.Adaptive(200.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        contentPadding = contentPaddingValues
    ) {
        items(items = allLists, key = {list -> list.id}) { list ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
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
                    Text(text = list.title)
                    Text(text = list.subject, modifier = Modifier.padding(bottom = 8.dp))
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
            text = stringResource(R.string.no_list_message),
            style = MaterialTheme.typography.titleLarge
        )
        Button(
            shape = MaterialTheme.shapes.small,
            onClick = {
                homeScreenViewModel.setCreateDialogVisibility(true)
            }
        ) {
            Text(
                text = stringResource(R.string.create_a_list),
                style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun CreateListDialog(homeScreenViewModel: HomeScreenViewModel) {
    val homeScreenUIState = homeScreenViewModel.uiState.collectAsState()
    val titleState = homeScreenViewModel.titleSavedState
    val subjectState = homeScreenViewModel.subjectSavedState

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
                    label = {
                        Text(text = stringResource(R.string.title))
                    },
                    isError = homeScreenUIState.value.isError,
                    lineLimits = TextFieldLineLimits.SingleLine,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(Modifier.padding(8.dp))
                ErrorMessageDisplayWithinDialog(homeScreenUIState)

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
                        onClick = {
                            homeScreenViewModel.setCreateDialogVisibility(false)
                         },
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    Button(
                        onClick = {
                            homeScreenViewModel.createNewList(titleState.text, subjectState.text)
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

@Composable
fun ErrorMessageDisplayWithinDialog(homeScreenUIState: State<HomeScreenUIState>) {
    val errMsg = when {
        homeScreenUIState.value.isError -> R.string.error_message_no_title
        homeScreenUIState.value.hasSQLError -> R.string.error_message_duplicate_title
        else -> -1
    }

    if (errMsg != -1) {
        Text(
            text = stringResource(errMsg),
            color = Color.Red)
    }
}
