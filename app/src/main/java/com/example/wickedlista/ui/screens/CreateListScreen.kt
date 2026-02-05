package com.example.wickedlista.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.wickedlista.R


@Composable
fun NewListCreation(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 8.dp, 16.dp, 8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            state = rememberTextFieldState(),
            label = {
                Text(text = stringResource(R.string.title))
            },
            modifier = Modifier
                .fillMaxWidth()
        )
        TextField(
            state = rememberTextFieldState(),
            label = {
                Text(text = stringResource(R.string.subject))
            },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {},
                shape = MaterialTheme.shapes.large
            ) {
                Text(text = stringResource(R.string.cancel))
            }
            Button(
                onClick = {},
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    text = stringResource(R.string.create)
                )
            }
        }

    }
}

@Preview
@Composable
fun NewListCreationPreview() {
    NewListCreation()
}