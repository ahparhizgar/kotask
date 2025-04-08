package io.amirhparhizgar.kotask

import io.amirhparhizgar.kotask.list.FakeTaskRepository
import io.amirhparhizgar.kotask.taskoperation.DefaultTaskOperationComponent
import io.amirhparhizgar.kotask.test.util.MainDispatcherExtension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.properties.shouldHaveValue
import kotlinx.coroutines.flow.first

class TaskOperationsComponentTest : BehaviorSpec({
    coroutineTestScope = true
    isolationMode = IsolationMode.InstancePerLeaf
    extensions(MainDispatcherExtension())

    Given("a TaskOperationComponent") {
        val task = FakeTaskFactory.createNewlyAdded()
        val repo = FakeTaskRepository(listOf(task))

        suspend fun updatedTask() = repo.taskStream.first().first()

        val component = DefaultTaskOperationComponent(taskId = task.id, repository = repo)

        When("task is marked as done") {
            component.setDone(true).join()
            Then("task should be marked as done in repository") {
                updatedTask()::isDone shouldHaveValue true
            }
        }

        When("task is marked as important") {
            component.setImportance(true).join()
            Then("task should be marked as important in repository") {
                updatedTask()::isImportant shouldHaveValue true
            }
        }

        When("task title is changed") {
            component.setTitle("New Title").join()
            Then("task title should be updated in repository") {
                updatedTask()::title shouldHaveValue "New Title"
            }
        }
    }
})
