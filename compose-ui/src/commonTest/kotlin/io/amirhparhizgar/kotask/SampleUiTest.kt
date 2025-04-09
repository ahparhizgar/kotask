package io.amirhparhizgar.kotask

import androidx.compose.ui.test.ExperimentalTestApi
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
                    val repository = FakeTaskRepository()
                    DefaultMultiPaneTasksComponent(
                        componentContext = DefaultComponentContext(lifecycle = lifecycle),
                        listComponentFactory = { c, onEdit ->
                            DefaultTaskListComponent(
                                componentContext = c,
                                repo = repository,
                                taskItemFactory = { t, onEdit2 ->
                                    DefaultTaskItemComponent(
                                        task = t,
                                        taskOperationComponent = DefaultTaskOperationComponent(
                                            taskId = t.id,
                                            repository = repository,
                                        ),
                                        onEdit = onEdit2,
                                    )
                                },
                                onEditRequested = onEdit,
                            )
                        },
                        editComponentFactory = { c, id ->
                            DefaultEditTaskComponent(
                                id = id,
                                context = c,
                                repository = repository,
                                taskOperationComponent = DefaultTaskOperationComponent(
                                    taskId = id,
                                    repository = repository,
                                ),
                            )
                        },
                        addComponentFactory = { c ->
                            DefaultAddTaskComponent(context = c, repository = repository)
                        },
                    )
                }
            setContent {
                RootContent(root)
            }
            lifecycle.resume()
        }
}
