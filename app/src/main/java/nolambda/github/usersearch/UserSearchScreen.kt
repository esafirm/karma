package nolambda.github.usersearch

import android.widget.Toast
import kotlinx.android.synthetic.main.screen_user_search.*
import nolambda.github.usersearch.utils.onTextChange
import nolambda.github.usersearch.utils.setVisible
import nolambda.kommonadapter.attach
import stream.nolambda.karma.fetch
import stream.nolambda.karma.ui.ActivityScreen
import stream.nolambda.karma.ui.xml

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

        btnBack.setOnClickListener {
            presenter.back()
        }

        btnForward.setOnClickListener {
            presenter.forward()
        }

        inpSearch.onTextChange(debounceTime = 300) {
            presenter.search(it)
        }
    }

    private fun render(state: UserSearchState) {
        recycler.setVisible(state.isLoading.not(), animate = true)
        progressBar.setVisible(state.isLoading, animate = true)

        adapter.pushData(state.users)

        state.err?.fetch {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }
}