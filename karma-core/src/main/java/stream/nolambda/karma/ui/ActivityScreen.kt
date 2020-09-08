package stream.nolambda.karma.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import stream.nolambda.karma.KarmaPresenter

abstract class ActivityScreen : AppCompatActivity(),
    ContextProvider {

    private val presenterHolder: PresenterHolder by lazy {
        ViewModelProvider(this).get(PresenterHolder::class.java)
    }

    abstract fun createView(): ScreenViewProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createView().invoke(layoutInflater, null))
        onViewCreated()
    }

    open fun onViewCreated() {}

    override val ctx: Context
        get() = this

    @Suppress("UNCHECKED_CAST")
    fun <T : KarmaPresenter<*>> createPresenter(block: () -> T): T {
        val currentPresenter = presenterHolder.presenter ?: block()
        presenterHolder.presenter = currentPresenter
        return currentPresenter as T
    }
}