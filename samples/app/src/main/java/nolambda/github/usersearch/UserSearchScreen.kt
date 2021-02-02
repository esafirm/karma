package nolambda.github.usersearch

import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import nolambda.github.usersearch.databinding.ScreenUserSearchBinding
import nolambda.github.usersearch.playground.PlaygroundScreen
import nolambda.github.usersearch.utils.onTextChange
import nolambda.github.usersearch.utils.setVisible
import nolambda.kommonadapter.attach
import stream.nolambda.karma.bind
import stream.nolambda.karma.differ.renderer
import stream.nolambda.karma.timetravel.TimeTravelEvent
import stream.nolambda.karma.timetravel.TimeTravelEventManager
import stream.nolambda.karma.timetravel.dashboard.TimeTravelDashboard
import stream.nolambda.karma.ui.screens.ActivityScreen
import stream.nolambda.karma.ui.screens.viewBinding

class UserSearchScreen : ActivityScreen() {

    companion object {
        private const val MENU_TIME_TRAVEL_DASHBOARD = 111
        private const val MENU_PLAYGROUND = 222
    }

    private val adapter by lazy { UserAdapter(this) }

    override val viewProvider = viewBinding(ScreenUserSearchBinding::inflate)

    override fun onViewCreated() {
        val renderer = createRenderer(viewProvider.getBinding())
        bind(
            presenter = { UserSearchPresenter() },
            render = renderer::render
        )
    }

    private fun createRenderer(binding: ScreenUserSearchBinding) =
        renderer<UserSearchState, UserSearchPresenter> {
            init {
                binding.recycler.attach(
                    adapter = adapter,
                    onBottomReached = { loadNextPage() }
                )

                binding.btnBack.setOnClickListener {
                    TimeTravelEventManager.dispatchEvent(TimeTravelEvent.Backward)
                }

                binding.btnForward.setOnClickListener {
                    TimeTravelEventManager.dispatchEvent(TimeTravelEvent.Forward)
                }

                binding.inpSearch.onTextChange(debounceTime = 300) { query ->
                    search(query)
                }
            }
            diff(UserSearchState::isLoading) {
                binding.recycler.setVisible(it.not(), animate = true)
                binding.progressBar.setVisible(it, animate = true)
            }
            event(UserSearchState::err) {
                val appContext = binding.root.context.applicationContext
                Toast.makeText(appContext, it.message, Toast.LENGTH_SHORT).show()
            }
            always {
                adapter.pushData(it.users)
            }
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(0, MENU_TIME_TRAVEL_DASHBOARD, 1, "Time Travel Dashboard")
        menu?.add(1, MENU_PLAYGROUND, 1, "Playground")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            MENU_TIME_TRAVEL_DASHBOARD -> TimeTravelDashboard.show(supportFragmentManager)
            MENU_PLAYGROUND -> PlaygroundScreen.start(this)
        }
        return super.onOptionsItemSelected(item)
    }
}