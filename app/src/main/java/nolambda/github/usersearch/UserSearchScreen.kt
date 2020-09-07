package nolambda.github.usersearch

import android.widget.Toast
import kotlinx.android.synthetic.main.screen_user_search.*
import stream.nolambda.karma.ActivityScreen
import stream.nolambda.karma.fetch
import stream.nolambda.karma.xml
import nolambda.github.usersearch.utils.onTextChange
import nolambda.github.usersearch.utils.setVisible
import nolambda.kommonadapter.attach

class UserSearchScreen : ActivityScreen() {

    private val presenter by lazy {
        createPresenter { UserSearchPresenter() }
    }

    private val adapter by lazy { UserAdapter(this) }

    override fun createView() = xml(R.layout.screen_user_search)

    override fun onViewCreated() {
        presenter.attach(this, ::render)

        recycler.attach(
            adapter = adapter,
            onBottomReached = { presenter.loadNextPage() }
        )

        inpSearch.onTextChange(debounceTime = 300) {
            presenter.search(it)
        }
    }

    private fun render(state: UserSearchState) {
        recycler.setVisible(state.isLoading.not())
        progressBar.setVisible(state.isLoading)

        adapter.pushData(state.users)

        state.err?.fetch {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }
}