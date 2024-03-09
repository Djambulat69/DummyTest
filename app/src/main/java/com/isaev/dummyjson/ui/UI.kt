package com.isaev.dummyjson.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.isaev.dummyjson.R

@Composable
fun ErrorMessage(onRetry: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.wrong_message)
        )
        Button(onClick = { onRetry() }) {
            Text(text = stringResource(id = R.string.retry))
        }
    }
}