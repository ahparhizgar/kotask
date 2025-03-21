package io.amirhparhizgar.kotask

import io.amirhparhizgar.kotask.list.FakeTaskRepository
import io.amirhparhizgar.kotask.test.util.createComponentContext
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.properties.shouldHaveValue
import io.kotest.matchers.properties.shouldMatch
import io.kotest.matchers.shouldBe

class AddTaskComponentTest : BehaviorSpec({
    coroutineTestScope = true
    Given("AddTaskComponent") {
        val component: AddTaskComponent = DefaultAddTaskComponent(
            context = createComponentContext(),
            repository = FakeTaskRepository(),
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

        When("the task is added") {
            component.onAddClick().join()
            Then("state is updated") {
                component.state.value::addedTask shouldMatch {
                    shouldNotBeNull()
                }
            }
        }
    }
})
