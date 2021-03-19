package nolambda.github.usersearch.playground

import stream.nolambda.karma.differ.Async

data class NestedState(
    val componentFirst: String = "",
    val componentSecond: String = "123"
)

data class PlaygroundState(
    val log: String = "",
    val nestedState: NestedState = NestedState(),
    val asyncValue: Async<String> = Async()
)