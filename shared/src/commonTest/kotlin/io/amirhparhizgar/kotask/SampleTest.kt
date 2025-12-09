package io.amirhparhizgar.kotask

import io.kotest.core.spec.style.BehaviorSpec

class SampleTest : BehaviorSpec({
    given("sample given") {
        println("sample given")
        `when`("first when") {
            println("first when")
            then("first then") {
                println("first then")
            }
        }
        `when`("second when") {
            println("second when")
            then("second then") {
                println("second then")
            }
        }
    }
})