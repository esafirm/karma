package nolambda.github.usersearch

import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.screen_user_search.view.*
import nolambda.github.usersearch.utils.onTextChange
import nolambda.github.usersearch.utils.setVisible
import nolambda.kommonadapter.attach
import stream.nolambda.karma.Karma
import stream.nolambda.karma.differ.ViewRenderer
import stream.nolambda.karma.differ.renderer
import stream.nolambda.karma.timetravel.TimeTravelEvent
import stream.nolambda.karma.timetravel.TimeTravelEventManager
import stream.nolambda.karma.timetravel.dashboard.TimeTravelDashboard
import stream.nolambda.karma.ui.ActivityScreen
import stream.nolambda.karma.ui.xml

class UserSearchScreen : ActivityScreen() {

    companion object {
        private const val MENU_TIME_TRAVEL_DASHBOARD = 111
    }

    override fun createView() = xml(R.layout.screen_user_search)

    override fun onViewCreated(view: View) {
        val renderer = UserSearchRenderer(view)
        Karma.bind(
            owner = this,
            presenterCreator = { UserSearchPresenter() },
            render = renderer::render
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(0, MENU_TIME_TRAVEL_DASHBOARD, 1, "Time Travel Dashboard")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == MENU_TIME_TRAVEL_DASHBOARD) {
            TimeTravelDashboard.start(this)
        }
        return super.onOptionsItemSelected(item)
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
                TimeTravelEventManager.dispatchEvent(TimeTravelEvent.Backward)
            }

            view.btnForward.setOnClickListener {
                TimeTravelEventManager.dispatchEvent(TimeTravelEvent.Forward)
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