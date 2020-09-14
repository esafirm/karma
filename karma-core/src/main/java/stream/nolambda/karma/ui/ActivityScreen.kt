package stream.nolambda.karma.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

abstract class ActivityScreen : AppCompatActivity(), ContextProvider {

    abstract fun createView(): ScreenViewProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = createView().invoke(layoutInflater, null)
        setContentView(view)
        onViewCreated(view)
    }

    open fun onViewCreated(view: View) {}

    override val ctx: Context
        get() = this
}