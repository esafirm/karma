package nolambda.github.usersearch.playground

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import stream.nolambda.karma.differ.Async

@Parcelize
data class NestedState(
    val componentFirst: String = "",
    val componentSecond: String = "123",
) : Parcelable

@Parcelize
data class PlaygroundState(
    val log: String = "",
    val nestedState: NestedState = NestedState(),
    val asyncValue: Async<String> = Async(),
) : Parcelable
