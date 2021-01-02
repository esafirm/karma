package stream.nolambda.karma.ui.screens

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class ActivityScreen : AppCompatActivity(), ContextProvider {

    abstract val viewProvider: ScreenViewProvider<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = viewProvider.inflate(layoutInflater, null)
        setContentView(view)
        onViewCreated()
    }

    open fun onViewCreated() {}

    override val ctx: Context
        get() = this
}