package com.example.cp3406_assessment_younghoocheon.ui.utility.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cp3406_assessment_younghoocheon.R
import com.example.cp3406_assessment_younghoocheon.data.repository.RaceError
import com.example.cp3406_assessment_younghoocheon.ui.theme.AppTheme

/** Centered spinner shown while the race is being loaded. */
@Composable
fun LoadingContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.loading_message),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/** Friendly full-screen message for a [RaceError] with a retry button. */
@Composable
fun ErrorContent(
    error: RaceError,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon: ImageVector
    val titleRes: Int
    val messageRes: Int
    val actionRes: Int

    when (error) {
        RaceError.NETWORK -> {
            icon = Icons.Filled.Warning
            titleRes = R.string.error_network_title
            messageRes = R.string.error_network_message
            actionRes = R.string.action_retry
        }
        RaceError.SERVER -> {
            icon = Icons.Filled.Warning
            titleRes = R.string.error_server_title
            messageRes = R.string.error_server_message
            actionRes = R.string.action_retry
        }
        RaceError.DATA -> {
            icon = Icons.Filled.Warning
            titleRes = R.string.error_data_title
            messageRes = R.string.error_data_message
            actionRes = R.string.action_retry
        }
        RaceError.NO_RACE -> {
            icon = Icons.Filled.Info
            titleRes = R.string.error_no_race_title
            messageRes = R.string.error_no_race_message
            actionRes = R.string.action_check_again
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(titleRes),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(messageRes),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRetry) {
            Text(text = stringResource(actionRes))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorContentPreview() {
    AppTheme {
        ErrorContent(error = RaceError.NETWORK, onRetry = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingContentPreview() {
    AppTheme {
        LoadingContent()
    }
}
