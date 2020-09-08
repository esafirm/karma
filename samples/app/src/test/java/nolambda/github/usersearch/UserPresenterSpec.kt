package nolambda.github.usersearch

import io.kotlintest.matchers.types.shouldBeNull
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import nolambda.github.usersearch.data.ApiInterface
import nolambda.github.usersearch.data.ApiResponse
import nolambda.github.usersearch.data.User
import nolambda.github.usersearch.utils.ApiErrorException
import retrofit2.mock.Calls

class UserPresenterSpec : PresenterSpec({

    val mockApi = mockk<ApiInterface>(relaxed = true)
    val sut = UserSearchPresenter(mockApi)

    "given empty query, it won't trigger load next page" {
        sut.loadNextPage()

        verify(exactly = 0) {
            sut.search(any(), any())
        }
    }

    "given empty query, it won't trigger search" {
        sut.search("")

        verify(exactly = 0) {
            sut.search(any(), any())
        }
    }

    "load next page should trigger search" {
        val fakeUsers = listOf(User("", "cool_user"))
        val fakeResponse = ApiResponse(1, fakeUsers)

        val expectedPage = sut.currentState.currentPage + 1
        val expectedQuery = sut.currentState.lastQuery

        every {
            mockApi.search(any(), any())
        } answers {
            Calls.response(fakeResponse)
        }

        sut.loadNextPage()

        verify {
            mockApi.search(expectedQuery, expectedPage)
        }

        sut.currentState.currentPage shouldBe expectedPage
        sut.currentState.lastQuery shouldBe expectedQuery
        sut.currentState.users shouldBe fakeUsers
        sut.currentState.err.shouldBeNull()
    }

    "given success response, it should load initial page" {
        val fakeUsers = listOf(User("", "not_cool_user"))
        val fakeResponse = ApiResponse(1, fakeUsers)

        val expectedQuery = "testing"
        val expectedPage = 1

        every {
            mockApi.search(any(), any())
        } answers {
            Calls.response(fakeResponse)
        }

        sut.search(expectedQuery, expectedPage)

        verify {
            mockApi.search(expectedQuery, expectedPage)
        }

        sut.currentState.run {
            lastQuery shouldBe expectedQuery
            users shouldBe fakeUsers
            err.shouldBeNull()
        }
    }

    "given error response, it should result in error state" {
        every {
            mockApi.search(any(), any())
        } answers {
            Calls.failure<ApiResponse<User>>(ApiErrorException(""))
        }

        sut.search("q")

        (sut.currentState.err != null) shouldBe true
    }
})