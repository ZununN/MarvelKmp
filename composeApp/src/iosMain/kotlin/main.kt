import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kz.zunun.marvel_kmp.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController {

    val root = remember { DefaultComponentContext(LifecycleRegistry()) }

    Box(Modifier.padding(top = 40.dp)) {
        App(root)
    }

}
