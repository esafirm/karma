package nolambda.github.usersearch

import androidx.lifecycle.LifecycleOwner
import nolambda.github.usersearch.data.Api
import nolambda.github.usersearch.data.ApiInterface
import nolambda.github.usersearch.data.User
import nolambda.github.usersearch.utils.call
import stream.nolambda.karma.Action
import stream.nolambda.karma.KarmaAction
import stream.nolambda.karma.differ.asSingleEvent
import stream.nolambda.karma.timetravel.TimeTravel
import stream.nolambda.karma.timetravel.TimeTravelAction
import stream.nolambda.karma.timetravel.TimeTravelEventListener
import stream.nolambda.karma.ui.UiPresenter

class UserSearchPresenter(
    private val action: KarmaAction<UserSearchState> = TimeTravel(Action { UserSearchState() }),
    private val api: ApiInterface = Api()
) : UiPresenter<UserSearchState>(action), TimeTravelEventListener {

    companion object {
        const val FIRST_PAGE = 1
    }

    override fun onAttach(owner: LifecycleOwner) {
        bindTimeTravel(owner, action as TimeTravelAction)
    }

    private fun showNextPage(newUsers: List<User>) = setState {
        val newList = (users + newUsers).distinctBy { u -> u.login }
        copy(users = newList, currentPage = currentPage + 1, isLoading = false)
    }

    private fun showInitialPage(users: List<User>) = setState {
        copy(users = users, currentPage = FIRST_PAGE, isLoading = false)
    }

    private fun showError(err: Throwable) = setState {
        copy(err = err.asSingleEvent(), isLoading = false)
    }

    private fun showEmptyPage() = setState { UserSearchState() }

    private fun setSearchState(isLoadingFirstPage: Boolean, lastQuery: String) = setState {
        copy(isLoading = isLoadingFirstPage, lastQuery = lastQuery)
    }

    fun loadNextPage() {
        val lastQuery = action.currentState.lastQuery
        val requestedPage = action.currentState.currentPage + 1
        search(lastQuery, requestedPage)
    }

    fun search(query: String, page: Int = FIRST_PAGE) {
        if (query.isBlank()) {
            showEmptyPage()
            return
        }

        val isLoadFirstPage = page == FIRST_PAGE
        setSearchState(isLoadFirstPage, query)

        api.search(query, page).call(
            onSuccess = {
                if (isLoadFirstPage) {
                    showInitialPage(it.items)
                } else {
                    showNextPage(it.items)
                }
            },
            onError = { showError(it) }
        )
    }
}