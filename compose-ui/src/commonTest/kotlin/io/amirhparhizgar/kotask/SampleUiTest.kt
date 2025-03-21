package io.amirhparhizgar.kotask

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.amirhparhizgar.kotask.list.DefaultTaskListComponent
import io.amirhparhizgar.kotask.list.FakeTaskRepository
import io.amirhparhizgar.kotask.taskoperation.DefaultTaskOperationComponent
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class SampleUiTest {
    @Test
    fun testBuyMilk() =
        runComposeUiTest {
            val lifecycle = LifecycleRegistry()
            val root =
                runOnUiThread {
                    val taskRepository = FakeTaskRepository()
                    DefaultTaskListComponent(
                        componentContext = DefaultComponentContext(lifecycle = lifecycle),
                        repo = taskRepository,
                        taskOperationFactory = { task ->
                            DefaultTaskOperationComponent(task, taskRepository)
                        },
                    )
                }
            setContent {
                RootContent(root)
            }
            lifecycle.resume()
            onNodeWithText("Buy milk").assertExists()
        }
}
