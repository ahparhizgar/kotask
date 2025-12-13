# Coding Style And Conventions

## Technologies and Frameworks

### Dependency Injection

- **Framework**: Koin
- Use `factoryOf` and `singleOf` for DSL-style module definitions
- Bind interfaces to implementations using `bind<Interface>()`

### Navigation and Architecture Components

- **Framework**: Decompose
- Use the Factory pattern for all components (see Component Factory Pattern section below)
- State management: Use `MutableValue<T>` from Decompose (NOT `MutableState`)
- `MutableValue` requires non-nullable types - use empty defaults (e.g., `MealPlan(emptyList())`)
- In Composables: Use `subscribeAsState()` to observe `MutableValue` changes

### UI

- **Framework**: Jetpack Compose with Material 3
- Follow Material Design 3 guidelines
- Use declarative UI patterns

### HTTP Client

- **Framework**: Ktor (configured, ready for Google Sheets integration)

## Component Factory Pattern

All Decompose components must follow this three-part pattern:

### 1. Interface with Nested Factory

```kotlin
interface SampleComponent {
    // there can be multiple states. but keep relevant states in a single data class.
    val state: MutableValue<State>

    fun complexEvent() {}
    fun simpleEvent() {
        // when functionality of an event is doesn't depend on 
        // dependencies, implement it here in the interface.
    }

    data class State(
        val sampleValue: String = "",
    )

    interface Factory {
        fun create(context: ComponentContext): SampleComponent
    }
}
```

### 2. Default Implementation (Production)

```kotlin
class DefaultSampleComponent(
    context: ComponentContext,
    private val repository: SampleRepository
) : ComponentContext by context, SampleComponent {
    override val state = MutableValue(SampleComponent.State())

    override fun complexEvent() {
        // Real implementation
    }

    class Factory(
        private val repository: SampleRepository
    ) : SampleComponent.Factory {
        override fun create(context: ComponentContext): SampleComponent =
            DefaultSampleComponent(context, repository)
    }
}
```

### 3. Fake Implementation (Testing/Previews)

```kotlin
class FakeSampleComponent(
    initialState: SampleComponent.State
) : SampleComponent {
    override val state = MutableValue(initialState)

    class Factory(
        private val initialState: SampleComponent.State
    ) : SampleComponent.Factory {
        override fun create(context: ComponentContext): SampleComponent =
            FakeSampleComponent(initialState)
    }
}
```

### Koin Registration

```kotlin
val appModule = module {
    factoryOf(DefaultSampleComponent::Factory) {
        bind<SampleComponent.Factory>()
    }
}
```

### Testing

Use Kotest BehaviorSpec style to test component logic.
here's an example that demonstrates how to use `testValue` for mutable state in tests and
`beforeTest` to set up state before each test case. after a `Given` or `When` block, there should be
the implementation of its corresponding test logic in the then block.
we only write assertions in the `Then` block.

use fake factories to create DTOs or domain models as needed.

```kotlin
class SampleTest : BehaviorSpec({
    Given("my var") {
        var myVar by testValue { 1 }
        When("set it to 2",) {
            beforeTest {
                myVar = 2
            }
            Then("should be 2") {
                myVar shouldBe 2
            }
        }
    }
})
```

### Fake factories

When ever you define domain models or DTOs, defin the it's fake factory below it as shown below.

```kotlin
@Serializable
data class SampleData(
    val id: Int,
    val name: String,
)

object FakeSampleDataFactory {
    fun create(
        id: Int = 1,
        name: String = "Sample Name",
    ) = SampleData(
        id = id,
        name = name,
    )
    fun createSpecific(): SampleData = create(
        id = 42,
        name = "Specific Sample",
    )

    fun createList(size: Int): List<SampleData> = listOf(
        create(id = 1, name = "Meaning ful name"),
        create(id = 2, name = "A fun name"),
    )
}
```

## Coding Conventions

- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Keep functions small and focused
- Add comments only for complex logic or non-obvious decisions
- Prefer immutability where possible

## Build and Test Commands

- **Build**: `./gradlew assembleDebug`
- **Test**: `./gradlew test`
- **Lint**: `./gradlew lint`

## Important Notes

- Notifications scheduled daily at 10:30 PM (22:30) to remind users to prepare tomorrow's meal
- Notification timing can be changed in `NotificationScheduler.kt` constant
- Meal plan data is currently hardcoded in `MealPlanRepository.kt`
- Future enhancement: Google Sheets integration using Ktor client
