package nolambda.github.usersearch

import nolambda.github.usersearch.data.Api
import nolambda.github.usersearch.data.ApiInterface
import nolambda.github.usersearch.data.User
import nolambda.github.usersearch.utils.call
import stream.nolambda.karma.StatePresenter
import stream.nolambda.karma.asSingleEvent

class UserSearchPresenter(
    private val api: ApiInterface = Api()
) : StatePresenter<UserSearchState>() {

    companion object {
        const val FIRST_PAGE = 1
    }

    override fun initialState(): UserSearchState = UserSearchState()

    private fun showNextPage(users: List<User>) = action {
        name = { "Show next page" }
        setState {
            val newList = (users + users).distinctBy { u -> u.login }
            copy(users = newList, currentPage = currentPage + 1, isLoading = false)
        }
    }

    private fun showInitialPage(users: List<User>) = action {
        name = { "Show initial page" }
        setState {
            copy(users = users, currentPage = FIRST_PAGE, isLoading = false)
        }
    }

    private fun showError(err: Throwable) = action {
        name = { "Show error" }
        setState {
            copy(err = err.asSingleEvent(), isLoading = false)
        }
    }

    private fun showEmptyPage() = action {
        setState { initialState() }
    }

    private fun setSearchState(isLoadingFirstPage: Boolean, lastQuery: String) = action {
        setState { copy(isLoading = isLoadingFirstPage, lastQuery = lastQuery) }
    }

    fun loadNextPage() {
        val lastQuery = currentState.lastQuery
        val requestedPage = currentState.currentPage + 1
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