package io.amirhparhizgar.kotask.list

import io.amirhparhizgar.kotask.database.DatabaseFactory
import io.amirhparhizgar.kotask.database.JvmInMemoryDriverFactory
import io.amirhparhizgar.kotask.test.util.MainDispatcherExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.properties.shouldHaveValue
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalStdlibApi::class)
class DatabaseTaskRepositoryTest : BehaviorSpec({
    coroutineTestScope = true
    extensions(MainDispatcherExtension())

    Given("empty in-memory db") {
        val db = DatabaseFactory(JvmInMemoryDriverFactory())
        val repository =
            DatabaseTaskRepository(
                databaseFactory = db,
                dispatcher = coroutineContext[CoroutineDispatcher.Key]!!,
            )
        When("adding a task") {
            val id = repository.addTask("Test")
            Then("it should be there") {
                val task = repository.get(id)
                task::id shouldHaveValue id
            }
            And("setting it's status to done") {
                repository.updateDoneStatus(id = id, isDone = true)
                Then("it should be done") {
                    val task = repository.taskStream.first().first { it.id == id }
                    task::isDone shouldHaveValue true
                }
            }
        }

        When("adding a task with due date") {
            val due = LocalDate(2025, 3, 24)
            val id = repository.addTask("Task with Due Date", due)
            Then("task should be added with correct due date") {
                val task = repository.get(id)
                task.title shouldBe "Task with Due Date"
                task.dueDate shouldBe due
            }
        }
    }
})
