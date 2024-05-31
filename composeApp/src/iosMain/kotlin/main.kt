import androidx.compose.ui.window.ComposeUIViewController
import kz.zunun.marvel_kmp.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
