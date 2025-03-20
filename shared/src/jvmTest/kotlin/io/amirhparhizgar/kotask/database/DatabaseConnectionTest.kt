package io.amirhparhizgar.kotask.database

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.properties.shouldHaveValue

class DatabaseConnectionTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerRoot

    Given("there is no db") {
        When("initializing a db") {
            createDatabase(DriverFactory())
            Then("initializes successfully") { }
        }
    }

    // TODO crate a empty db
    Given("there is a db") {
        val db = createDatabase(DriverFactory())
        When("adding a Todo") {
            db.toDoDatabaseQueries.add("Buy milk")
            And("retrieving it") {
                Then("retrieves successfully") {
                    val toDo = db.toDoDatabaseQueries.select(1).executeAsOne()
                    toDo::text shouldHaveValue "Buy milk"
                }
            }
        }
    }
})
