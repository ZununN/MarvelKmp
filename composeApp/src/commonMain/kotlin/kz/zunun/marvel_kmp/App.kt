package kz.zunun.marvel_kmp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.annotation.ExperimentalCoilApi
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import kotlinx.serialization.Serializable
import kz.zunun.character_detail.CharacterDetailComponent
import kz.zunun.character_detail.CharacterDetailScreen
import kz.zunun.characters.CharactersComponent
import kz.zunun.characters.CharactersScreen
import kz.zunun.characters.charactersModule
import kz.zunun.data.core.dataModule
import org.koin.compose.KoinApplication
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import okio.FileSystem

@OptIn(ExperimentalCoilApi::class)
@Composable
internal fun App(
    componentContext: ComponentContext,
) {
    setSingletonImageLoaderFactory { context ->
        getAsyncImageLoader(context)
    }

    KoinApplication(
        application = { modules(dataModule, charactersModule) }
    ) {
        val root = remember(componentContext) { RootComponent(componentContext) }

        Children(root.stack) {

            Box(Modifier.background(Color.White)) {
                when (val component = it.instance) {
                    is CharactersComponent -> CharactersScreen(component)
                    is CharacterDetailComponent -> CharacterDetailScreen(component)
                }
            }
        }
    }
}


class RootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext {

    private val navigator = StackNavigation<Config>()

    val stack = childStack(
        source = navigator,
        initialConfiguration = Config.CharactersScreen,
        handleBackButton = true,
        childFactory = ::child,
        serializer = Config.serializer()
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ) = when (config) {
        is Config.CharacterDetails -> CharacterDetailComponent(componentContext, config.id) {
            navigator.pop()
        }

        is Config.CharactersScreen -> CharactersComponent(componentContext) {
            navigator.push(Config.CharacterDetails(it))
        }
    }

}

@Serializable
sealed interface Config {

    @Serializable
    data object CharactersScreen : Config

    @Serializable
    data class CharacterDetails(val id: Int) : Config
}


fun getAsyncImageLoader(context: PlatformContext): ImageLoader {
    fun newDiskCache(): DiskCache {
        return DiskCache.Builder().directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "image_cache")
            .maxSizeBytes(1024L * 1024 * 1024) // 512MB
            .build()
    }

    return ImageLoader.Builder(context).memoryCachePolicy(CachePolicy.ENABLED).memoryCache {
        MemoryCache.Builder().maxSizePercent(context, 0.3).strongReferencesEnabled(true).build()
    }.diskCachePolicy(CachePolicy.ENABLED).networkCachePolicy(CachePolicy.ENABLED).diskCache {
        newDiskCache()
    }.crossfade(true).logger(DebugLogger()).build()
}
