package io.amirhparhizgar.kotask

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.amirhparhizgar.kotask.list.DefaultTaskListComponent
import io.amirhparhizgar.kotask.list.FakeTodoRepository
import io.amirhparhizgar.kotask.root.RootContent
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class SampleUiTest {
    @Test
    fun testBuyMilk() =
        runComposeUiTest {
            val lifecycle = LifecycleRegistry()
            val root =
                runOnUiThread {
                    DefaultTaskListComponent(
                        componentContext = DefaultComponentContext(lifecycle = lifecycle),
                        repo = FakeTodoRepository(),
                    )
                }
            setContent {
                RootContent(root)
            }
            lifecycle.resume()
            onNodeWithText("Buy milk").assertExists()
        }
}
