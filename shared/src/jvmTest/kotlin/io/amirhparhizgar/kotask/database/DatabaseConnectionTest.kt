package io.amirhparhizgar.kotask.database

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.properties.shouldHaveValue

class DatabaseConnectionTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerRoot

    Given("there is no db") {
        When("initializing a db, initializes successfully") {
            createDatabase(JvmInMemoryDriverFactory())
        }
    }

    Given("there is a db") {
        val db = createDatabase(JvmInMemoryDriverFactory())
        When("adding a Todo") {
            db.toDoDatabaseQueries.add("Buy milk")
            And("retrieving it") {
                val toDo = db.toDoDatabaseQueries.select(1).executeAsOne()
                Then("retrieves successfully") {
                    toDo::text shouldHaveValue "Buy milk"
                }
            }
        }
    }
})
