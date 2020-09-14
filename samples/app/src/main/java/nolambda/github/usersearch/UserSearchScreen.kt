package nolambda.github.usersearch

import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.screen_user_search.view.*
import nolambda.github.usersearch.utils.onTextChange
import nolambda.github.usersearch.utils.setVisible
import nolambda.kommonadapter.attach
import stream.nolambda.karma.Karma
import stream.nolambda.karma.differ.ViewRenderer
import stream.nolambda.karma.differ.renderer
import stream.nolambda.karma.ui.ActivityScreen
import stream.nolambda.karma.ui.xml

class UserSearchScreen : ActivityScreen() {

    override fun createView() = xml(R.layout.screen_user_search)

    override fun onViewCreated(view: View) {
        val renderer = UserSearchRenderer(view)
        Karma.bind(
            owner = this,
            presenterCreator = { UserSearchPresenter() },
            render = renderer::render
        )
    }
}

class UserSearchRenderer(view: View) : ViewRenderer<UserSearchState, UserSearchPresenter> {

    private val adapter by lazy { UserAdapter(view.context) }

    private val renderer = renderer<UserSearchState, UserSearchPresenter> {
        init {
            view.recycler.attach(
                adapter = adapter,
                onBottomReached = { loadNextPage() }
            )

            view.btnBack.setOnClickListener {
                back()
            }

            view.btnForward.setOnClickListener {
                forward()
            }

            view.inpSearch.onTextChange(debounceTime = 300) { query ->
                search(query)
            }
        }
        diff(UserSearchState::isLoading) {
            view.recycler.setVisible(it.not(), animate = true)
            view.progressBar.setVisible(it, animate = true)
        }
        event(UserSearchState::err) {
            val appContext = view.context.applicationContext
            Toast.makeText(appContext, it.message, Toast.LENGTH_SHORT).show()
        }
        always {
            adapter.pushData(it.users)
        }
    }

    override fun render(state: UserSearchState, action: UserSearchPresenter) {
        renderer.render(state, action)
    }
}