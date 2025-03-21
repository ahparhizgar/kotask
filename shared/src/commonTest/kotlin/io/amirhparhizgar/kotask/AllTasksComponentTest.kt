package io.amirhparhizgar.kotask

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.amirhparhizgar.kotask.list.DefaultTaskListComponent
import io.amirhparhizgar.kotask.list.FakeTaskRepository
import io.amirhparhizgar.kotask.taskoperation.DefaultTaskOperationComponent
import io.amirhparhizgar.kotask.test.util.MainDispatcherExtension
import io.amirhparhizgar.kotask.test.util.createComponentContext
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class)
class AllTasksComponentTest : BehaviorSpec({
    coroutineTestScope = true
    extensions(MainDispatcherExtension())

    Given("all tasks component with real children") {
        val repository = FakeTaskRepository(initialTasks = emptyList())
        val lifecycle = LifecycleRegistry()
        val allTasksComponent: AllTasksComponent = DefaultAllTasksComponent(
            componentContext = createComponentContext(lifecycle),
            listComponentFactory = {
                DefaultTaskListComponent(
                    componentContext = it,
                    repo = repository,
                    taskOperationFactory = {
                        DefaultTaskOperationComponent(it, repository)
                    },
                )
            },
            addComponentFactory = {
                DefaultAddTaskComponent(
                    context = it,
                    repository = repository,
                )
            },
        )
        lifecycle.resume()

        When("adding a task") {
            allTasksComponent.addComponent.onTitleChange("New Task")
            allTasksComponent.addComponent.onAddClick().join()

            Then("it should be in the list") {
                withClue("task list") {
                    allTasksComponent.listComponent.items.value shouldHaveSize 1
                }
                testCoroutineScheduler.advanceUntilIdle()
                allTasksComponent.listComponent.items.value
                    .first()
                    .task
                    .title shouldBe "New Task"
            }
        }
    }
})
