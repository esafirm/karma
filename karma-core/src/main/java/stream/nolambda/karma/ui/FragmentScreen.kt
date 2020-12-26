package stream.nolambda.karma.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class FragmentScreen : Fragment(), ContextProvider {

    abstract fun createView(): ScreenViewProvider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView().invoke(inflater, container)
    }

    override val ctx: Context
        get() = requireContext()
}