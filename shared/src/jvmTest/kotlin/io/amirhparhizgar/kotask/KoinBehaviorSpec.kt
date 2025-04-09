package io.amirhparhizgar.kotask

import io.kotest.core.spec.style.BehaviorSpec
import org.koin.test.KoinTest

abstract class KoinBehaviorSpec(body: KoinBehaviorSpec.() -> Unit = {}) : KoinTest,
    BehaviorSpec() {
    init {
        body()
    }
}
