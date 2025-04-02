package io.amirhparhizgar.kotask

import io.amirhparhizgar.kotask.list.FakeTaskRepository
import io.amirhparhizgar.kotask.test.util.createComponentContext
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.properties.shouldHaveValue
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalDate

class AddTaskComponentTest : BehaviorSpec({
    coroutineTestScope = true
    Given("AddTaskComponent") {
        val repository = FakeTaskRepository()
        val component: AddTaskComponent =
            DefaultAddTaskComponent(
                context = createComponentContext(),
                repository = repository,
            )

        When("the component is created") {
            Then("it should have an initial state") {
                component.state.value shouldBe AddTaskComponent.State()
            }
        }

        When("the title is updated") {
            component.onTitleChange("New Task")
            Then("the state should be updated") {
                component.state.value::title shouldHaveValue "New Task"
            }
        }

        When("the due date is updated") {
            val due = LocalDate(2025, 3, 24)
            component.onDueDateChange(due)
            Then("the state should be updated") {
                component.state.value::dueDate shouldHaveValue due
            }
        }

        When("the task is added with due date") {
            val due = LocalDate(2025, 3, 24)
            component.onTitleChange("New Task")
            component.onDueDateChange(due)
            component.onAddClick().join()
            Then("state is updated and task is added to repo") {
                val addedTask =
                    component.state.value.addedTask
                        .shouldNotBeNull()
                component.state.value::title shouldHaveValue ""
                component.state.value::dueDate shouldHaveValue null
                val tasks = repository.taskStream.first()
                tasks.last()::id shouldHaveValue addedTask.id
            }
        }
    }
})
