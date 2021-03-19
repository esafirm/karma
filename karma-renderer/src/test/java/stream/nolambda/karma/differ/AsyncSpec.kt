package stream.nolambda.karma.differ

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class AsyncSpec : StringSpec({

    "it should have a valid state" {
        Async<String>().isUninitialized shouldBe true
        Async.loading<String>().isLoading shouldBe true
        Async.failure<String>(Throwable("")).isError shouldBe true
        Async.success("a").isSuccess shouldBe true
    }

    "success should have a value" {
        val success = Async.success(123)
        success.get() shouldBe 123
        success.getOrNull() shouldBe 123
        success.getErrorOrNull() shouldBe null
    }

    "error should have a value" {
        val throwable = Throwable()
        val error = Async.failure<String>(throwable)
        error.getErrorOrNull() shouldBe throwable
        error.getOrNull() shouldBe null
    }

    "loading should not have a value" {
        val loading = Async.loading<String>()
        loading.getOrNull() shouldBe null
        loading.getErrorOrNull() shouldBe null
    }
})
