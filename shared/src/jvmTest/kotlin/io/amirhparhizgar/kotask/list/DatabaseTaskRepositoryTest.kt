package io.amirhparhizgar.kotask.list

import io.amirhparhizgar.kotask.database.DatabaseFactory
import io.amirhparhizgar.kotask.database.JvmInMemoryDriverFactory
import io.amirhparhizgar.kotask.test.util.MainDispatcherExtension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.properties.shouldHaveValue
import kotlinx.coroutines.flow.first

class DatabaseTaskRepositoryTest : BehaviorSpec({
    coroutineTestScope = true
    isolationMode = IsolationMode.InstancePerRoot
    extensions(MainDispatcherExtension())

    Given("one task in-memory db") {
        val db = DatabaseFactory(JvmInMemoryDriverFactory())
        val repo = DatabaseTaskRepository(db)
        repo.addTask("Test")
        When("setting it to done") {
            repo.updateTaskIsDone(1, true)
            Then("it should be done") {
                val task = repo.tasks.first().first()
                task::isDone shouldHaveValue true
            }
        }
    }
})
