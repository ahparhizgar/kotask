package io.amirhparhizgar.kotask

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.amirhparhizgar.kotask.list.DefaultTaskListComponent
import io.amirhparhizgar.kotask.list.FakeTaskRepository
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class SampleUiTest {
    @Test
    fun testBuyMilk() =
        runComposeUiTest {
            val lifecycle = LifecycleRegistry()
            val root =
                runOnUiThread {
                    val repository = FakeTaskRepository()
                    DefaultMultiPaneTasksComponent(
                        componentContext = DefaultComponentContext(lifecycle = lifecycle),
                        listComponentFactory =
                            DefaultTaskListComponent.Factory(
                                repo = repository,
                                taskItemFactory =
                                    DefaultTaskItemComponent.Factory(
                                        taskRepository = repository,
                                    ),
                            ),
                        editComponentFactory =
                            DefaultEditTaskComponent.Factory(
                                repository = repository,
                            ),
                        addComponentFactory = DefaultAddTaskComponent.Factory(repository = repository),
                    )
                }
            setContent {
                RootContent(root)
            }
            lifecycle.resume()
        }
}
