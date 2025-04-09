package io.amirhparhizgar.kotask

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.amirhparhizgar.kotask.list.DefaultTaskListComponent
import io.amirhparhizgar.kotask.list.FakeTaskRepository
import io.amirhparhizgar.kotask.taskoperation.DefaultTaskOperationComponent
import io.amirhparhizgar.kotask.test.util.MainDispatcherExtension
import io.amirhparhizgar.kotask.test.util.createComponentContext
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.properties.shouldHaveValue
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalStdlibApi::class)
class TaskListOperationsIntegrationTest : BehaviorSpec({
    coroutineTestScope = true
    isolationMode = IsolationMode.InstancePerLeaf
    extensions(MainDispatcherExtension())

    Given("A TaskListComponent with real operations") {
        val lifecycleRegistry = LifecycleRegistry()
        val repo = FakeTaskRepository()
        val context = createComponentContext(lifecycle = lifecycleRegistry)

        val component = DefaultTaskListComponent(
            componentContext = context,
            repo = repo,
            taskItemFactory = { task, _ ->
                DefaultTaskItemComponent(
                    task = task,
                    taskOperationComponent = DefaultTaskOperationComponent(
                        taskId = task.id,
                        repository = repo,
                    ),
                    onEdit = {},
                )
            },
            onEditRequested = { },
        )
        lifecycleRegistry.resume()
        testCoroutineScheduler.advanceUntilIdle()
        val firstTask = component
            .items
            .value
            .first()

        When("task is marked as done") {
            firstTask.setDone(true).join()
            Then("task should be marked as done in repository") {
                repo.taskStream
                    .first()
                    .first()::isDone shouldHaveValue true
            }
        }
    }
})
