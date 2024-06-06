package kz.zunun.characters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.zunun.characters.SubPagingInfo.*
import kz.zunun.characters.model.CharacterUI
import kz.zunun.ui_kit.view.AsyncImage
import kz.zunun.ui_kit.view.ErrorView
import kz.zunun.ui_kit.view.LoadingScreen


@Composable
fun CharactersScreen(component: CharactersComponent) {
    val state by component.state.collectAsState()

    Content(state = state) {
        component.dispatch(it)
    }
}

@Composable
private fun Content(
    state: State,
    action: (Action) -> Unit,
) {
    when (state) {
        is State.Error -> ErrorView {
            action(Action.FetchCharacters)
        }

        is State.Loading -> LoadingScreen()
        is State.Success -> LazyColumn(Modifier.fillMaxSize()) {
            items(state.characters) { character ->
                CharacterItem(character) {
                    action(Action.OnItemClick(it))
                }
            }

            item {
                SideEffect {
                    action(Action.DownScroll)
                }
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(24.dp), Alignment.Center
                ) {
                    when (state.subPagingInfo) {
                        NotData -> Unit
                        Loading -> CircularProgressIndicator(Modifier.size(50.dp))
                        Error -> ErrorItem(action)
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorItem(action: (Action) -> Unit) {
    Column {
        Text(text = "Oooops, some error", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = { action(Action.FetchCharacters) }) {
            Text(
                text = "Update", style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun CharacterItem(item: CharacterUI, onClick: (Int) -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick(item.id) }) {

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                url = item.imageUrl,
                modifier = Modifier
                    .padding(8.dp)
                    .size(60.dp),
            )

            Text(text = item.name, style = MaterialTheme.typography.titleMedium)
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Gray)
        )
    }
}
