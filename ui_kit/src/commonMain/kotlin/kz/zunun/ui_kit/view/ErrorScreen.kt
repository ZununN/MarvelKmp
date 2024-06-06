package kz.zunun.ui_kit.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ErrorView(update: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column {

            Text(text = "Oooops, some error", fontSize = 44.sp)

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = update) {
                Text(text = "Update", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
fun EmptyScreen(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center, content = content
    )
}

@Composable
fun LoadingScreen() {
    EmptyScreen {
        CircleLoader()
    }
}