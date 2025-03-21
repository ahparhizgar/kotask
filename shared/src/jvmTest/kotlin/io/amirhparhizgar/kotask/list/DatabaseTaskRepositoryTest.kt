package io.amirhparhizgar.kotask.list

import io.amirhparhizgar.kotask.database.DatabaseFactory
import io.amirhparhizgar.kotask.database.JvmInMemoryDriverFactory
import io.amirhparhizgar.kotask.test.util.MainDispatcherExtension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.properties.shouldHaveValue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalStdlibApi::class)
class DatabaseTaskRepositoryTest : BehaviorSpec({
    coroutineTestScope = true
    isolationMode = IsolationMode.InstancePerRoot
    extensions(MainDispatcherExtension())

    Given("empty in-memory db") {
        val db = DatabaseFactory(JvmInMemoryDriverFactory())
        val repo = DatabaseTaskRepository(
            databaseFactory = db,
            dispatcher = coroutineContext[CoroutineDispatcher.Key]!!,
        )
        When("adding a task") {
            val id = repo.addTask("Test")
            Then("it should be there") {
                val task = repo.get(id)
                task::id shouldHaveValue id
            }
            And("setting it's status to done") {
                repo.updateDoneStatus(id = id, isDone = true)
                Then("it should be done") {
                    val task = repo.tasks.first().first { it.id == id }
                    task::isDone shouldHaveValue true
                }
            }
        }
    }
})
