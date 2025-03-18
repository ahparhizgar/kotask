package io.amirhparhizgar.kotask.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.amirhparhizgar.kotask.createComponentContext
import io.amirhparhizgar.kotask.testComponent
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.collections.shouldHaveSize

class TodoListComponentTest : FunSpec({
    extension(MainDispatcherExtension())

    lateinit var component: DefaultTodoListComponent
    lateinit var context: ComponentContext
    lateinit var lifecycleRegistry: LifecycleRegistry
    lateinit var repo: FakeTodoRepository

    beforeEach {
        lifecycleRegistry = LifecycleRegistry()
        repo = FakeTodoRepository()
        context = createComponentContext(lifecycle = lifecycleRegistry)
        component = DefaultTodoListComponent(context, repo)
    }

    testComponent("creates successfully") {
        component.items.value shouldHaveSize 0
    }

    testComponent("fetches list") {
        lifecycleRegistry.resume()
        testCoroutineScheduler.runCurrent()
        component.items.value shouldHaveSize 2
    }
})
