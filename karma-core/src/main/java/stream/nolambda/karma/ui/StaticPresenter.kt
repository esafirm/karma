package stream.nolambda.karma.ui

class StaticPresenter<State>(private val state: State) : UiPresenter<State>() {
    override fun initialState(): State = state
}