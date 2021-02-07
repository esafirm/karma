package stream.nolambda.karma.ui

class StaticPresenter<State>(private val staticState: State) : UiPresenter<State>() {
    override fun initialState(): State = staticState
}