package io.amirhparhizgar.kotask

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.amirhparhizgar.kotask.list.DefaultTaskListComponent
import io.amirhparhizgar.kotask.list.FakeTaskRepository
import io.amirhparhizgar.kotask.test.util.MainDispatcherExtension
import io.amirhparhizgar.kotask.test.util.createComponentContext
import io.amirhparhizgar.kotask.test.util.testComponent
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.collections.shouldHaveSize

@OptIn(ExperimentalStdlibApi::class)
class TaskListComponentTest : FunSpec({
    extension(MainDispatcherExtension())
    coroutineTestScope = true

    lateinit var component: DefaultTaskListComponent
    lateinit var context: ComponentContext
    lateinit var lifecycleRegistry: LifecycleRegistry
    lateinit var repo: FakeTaskRepository

    beforeEach {
        lifecycleRegistry = LifecycleRegistry()
        repo = FakeTaskRepository()
        context = createComponentContext(lifecycle = lifecycleRegistry)
        component = DefaultTaskListComponent(
            componentContext = context,
            repo = repo,
            taskItemFactory = FakeTaskItemComponent.Factory(),
            onEditRequested = {},
        )
    }

    testComponent("creates successfully") {
        component.items.value.items shouldHaveSize 0
    }

    testComponent("fetches list") {
        lifecycleRegistry.resume()
        testCoroutineScheduler.runCurrent()
        component.items.value.items shouldHaveSize 2
    }
})
