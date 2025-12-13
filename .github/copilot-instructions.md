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

### Testing

- **Framework**: Kotest with BehaviorSpec style
- Use given/when/then structure for all tests
- Example:

```kotlin
class ExampleTest : BehaviorSpec({
    Given("a component with initial state") {
        When("an action is performed") {
            Then("the expected outcome occurs") {
                // test body
            }
        }
    }
})
```

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
    factoryOf(DefaultSampleComponent::Factory) { bind<SampleComponent.Factory>() }
}
```

### Benefits

- Testability: Easy fake implementations without mocking
- Previews: Compose previews with predefined states
- Type safety: Compile-time component creation guarantees

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
