package kz.zunun.character_detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.zunun.ui_kit.view.AsyncImage
import kz.zunun.ui_kit.view.CircleLoader
import kz.zunun.ui_kit.view.EmptyScreen
import kz.zunun.ui_kit.view.ErrorView


@Composable
fun CharacterDetailScreen(component: CharacterDetailComponent) {
    val state by component.state.collectAsState()

    Column {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Text(
                text = "back",
                modifier = Modifier
                    .clickable { component.backClick.invoke() }
                    .padding(20.dp)
            )
        }

        when (val current = state) {
            State.Error -> ErrorView {
                component.dispatch(Action.FetchInfo)
            }

            State.Loading -> EmptyScreen { CircleLoader() }
            is State.Success -> SuccessState(state = current)
        }
    }

}

@Composable
private fun SuccessState(state: State.Success) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = state.name,
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 44.sp,
            letterSpacing = 3.sp,
            modifier = Modifier.padding(top = 24.dp),
        )

        AsyncImage(
            url = state.imageUrl,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        Text(
            text = state.description.orEmpty(),
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraLight,
            modifier = Modifier
                .padding(24.dp)
        )
    }
}