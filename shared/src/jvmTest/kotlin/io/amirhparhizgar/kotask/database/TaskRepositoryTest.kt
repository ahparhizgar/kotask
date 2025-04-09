package io.amirhparhizgar.kotask.database

import io.amirhparhizgar.kotask.KoinBehaviorSpec
import io.amirhparhizgar.kotask.di.AppModule
import io.amirhparhizgar.kotask.list.DatabaseTaskRepository
import io.amirhparhizgar.kotask.test.util.MainDispatcherExtension
import io.kotest.core.test.isRootTest
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.properties.shouldHaveValue
import kotlinx.coroutines.test.StandardTestDispatcher
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.get

@OptIn(ExperimentalStdlibApi::class)
class TaskRepositoryTest : KoinBehaviorSpec({
    extensions(MainDispatcherExtension())
    beforeTest {
        if (it.isRootTest()) {
            startKoin {
                modules(
                    AppModule,
                    module {
                        factory<DriverFactory> {
                            JvmInMemoryDriverFactory()
                        }
                    },
                )
            }
        }
    }
    coroutineTestScope = true
    context("there is a db") {
        val repository = DatabaseTaskRepository(
            databaseFactory = get(),
            dispatcher = StandardTestDispatcher(testCoroutineScheduler),
        )

        suspend fun task() = repository.get("1")

        Given("empty repository") {
            When("adding a Task") {
                repository.addTask(title = "new task", dueDate = null)
                And("retrieving it") {
                    Then("updates successfully") {
                        task()::title shouldHaveValue "new task"
                    }
                }
            }
        }
        Given("repository with a task") {
            When("updateDoneStatus") {
                repository.updateDoneStatus(id = "1", isDone = true)
                Then("updates successfully") {
                    task()::isDone shouldHaveValue true
                }
            }

            When("setImportant") {
                repository.setImportant(id = "1", important = true)
                Then("updates successfully") {
                    task()::isImportant shouldHaveValue true
                }
            }

            When("updateTitle") {
                repository.updateTitle(id = "1", title = "new title")
                Then("updates successfully") {
                    task()::title shouldHaveValue "new title"
                }
            }
        }
    }
})
