package io.amirhparhizgar.kotask

import com.arkivanov.decompose.ExperimentalDecomposeApi
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
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first

@OptIn(
    ExperimentalStdlibApi::class,
    ExperimentalCoroutinesApi::class,
    ExperimentalDecomposeApi::class,
)
class MultipaneTasksComponentTest : BehaviorSpec({
    coroutineTestScope = true
    extensions(MainDispatcherExtension())

    Given("all tasks component with real children, resumed") {
        val repository = FakeTaskRepository(initialTasks = emptyList())
        val lifecycle = LifecycleRegistry()
        val multiPaneTasksComponent: MultiPaneTasksComponent = DefaultMultiPaneTasksComponent(
            componentContext = createComponentContext(lifecycle),
            listComponentFactory = {
                DefaultTaskListComponent(
                    componentContext = it,
                    repo = repository,
                    taskItemFactory = { t ->
                        DefaultTaskItemComponent(
                            task = t,
                            taskOperationComponent = DefaultTaskOperationComponent(
                                taskId = t.id,
                                repository = repository,
                            ),
                            onEdit = onEdit,
                        )
                    },
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
            addComponentFactory = {
                DefaultAddTaskComponent(
                    context = it,
                    repository = repository,
                )
            },
        )
        lifecycle.resume()

        When("adding a task") {
            multiPaneTasksComponent.addComponent.onTitleChange("New Task")
            multiPaneTasksComponent.addComponent.onAddClick().join()

            Then("it should be in the list") {
                val listComponent = multiPaneTasksComponent.panels.value.details!!
                    .instance
                withClue("task list") {
                    listComponent.items.value shouldHaveSize 1
                }
                testCoroutineScheduler.advanceUntilIdle()
                listComponent.items.value
                    .first()
                    .task
                    .title shouldBe "New Task"
            }
        }

        And("contains a task") {
            repository.addFakeTask()
            val task = repository.taskStream.first().last()
            When("requesting edit") {
                multiPaneTasksComponent.openDetails(task)
                Then("edit component should be present") {
                    val instance = multiPaneTasksComponent.panels.value.extra
                        ?.instance
                        .shouldNotBeNull()
                    val loadingTask = instance.task.value
                    withClue("loading task") {
                        loadingTask.shouldBeTypeOf<LoadingTask.Loaded>()
                        loadingTask.task shouldBeEqual task
                    }
                }
            }
        }
    }
})
