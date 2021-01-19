## Karma

A library that provide an opinionated way to create Android app with MVI pattern.
It supports React's-ish time travel debugging and diff render.

## Gradle

In your root `build.gradle` 

```groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

In your module `build.gradle`

```groovy
dependencies {
  implementation 'com.github.esafirm.karma:karma-core:1.0.0'
  implementation 'com.github.esafirm.karma:karma-renderer:1.0.0'
  implementation 'com.github.esafirm.karma:karma-timetravel:1.0.0'
  implementation 'com.github.esafirm.karma:karma-timetravel-dashboard:1.0.0'
}
```

## Modules

The library currently separated into four modules:

1. Core
2. Renderer
3. Time Travel
4. Time Travel Dashboard

If we create a dependency tree, it would look like this: 

```
        core
      ___/\___
     /        \
renderer     time travel
                \
            time travel dashboard
```

 - `core` module is standalone
 - `renderer` needs core
 - `time travel` needs core
 - `time travel dasboard` needs core & time travel 

## Usage

### Core

Karma core introduce how you can create an MVI-ish application with minimal setup.

To create a screen, what you need is:

1. A State
2. A Presenter
3. An Android Component to bind it all together

State refer to a **view state**. For example we have this simple state

```kotlin
data class SimpleScreenState(
	val textLineOne: String,
	val textLineTwo: String
)
```

Presenter is a place that we call all our action that change a state.

```kotlin
class SimplePresenter : UiPresenter<SimpleScreenState>(action) {

	override fun initialState() = SimpleScreenState("a", "b")

	// We change textLineOne and retain our current textLineTwo
	fun changeLineOne(text: String) = setState {
		copy(textLineOne = text)
	}
}
```

To initialize Karma, you need to now os how to bind all the component together

```kotlin
fun <S, P : KarmaPresenter<S>> bind(
        lifecycleOwner: LifecycleOwner,
        viewModelStoreOwner: ViewModelStoreOwner,
        presenterCreator: () -> P,
        render: (S, P) -> Unit
	)
```

And this is an example of `Karma.bind` called in `AppCompatActivity`

```kotlin
Karma.bind(
	lifecycleOwner = this,
	viewModelStoreOwner = this,
	presenterCreator = { SimplePresenter() },
	render = { state, presenter -> 
	  // do render
	}
)
```

Or, we could use the extension function to make it more simple

```kotlin
bind(
  presenterCreator = { SimplePresenter() },
  render = renderer::render
)
```

### Renderer

Renderer enable us to control on how we can render state

```kotlin
renderer<State, Presenter> {
	init {
		// This will only called once.
		// Could be used to intialize some views or setup the listener
	}
	diff(State::data) {
		// This will only be called when state.data is changed
	}
	event(State::event) {
		// This will use special state called SingleEvent<E> that have internal flag
		// when the value is fetched
	}
	always {
		// This will be always called
	}
}
```

### Time Travel

TBD

### Time Travel Dashboard

TBD

## Sample

You can find complete usage of Karma in `:samples:app` module

## License

MIT @ Esa Firman