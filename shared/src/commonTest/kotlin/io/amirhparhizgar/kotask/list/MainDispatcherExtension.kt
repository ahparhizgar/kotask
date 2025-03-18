package io.amirhparhizgar.kotask.list

import io.kotest.core.extensions.Extension
import io.kotest.core.listeners.AfterEachListener
import io.kotest.core.listeners.BeforeEachListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

class MainDispatcherExtension(private val dispatcher: TestDispatcher = StandardTestDispatcher()) :
    Extension, BeforeEachListener, AfterEachListener {
    override suspend fun beforeEach(testCase: TestCase) {
        Dispatchers.setMain(dispatcher)
    }

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        Dispatchers.resetMain()
    }
}