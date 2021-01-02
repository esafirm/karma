package stream.nolambda.karma.ui.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.persistableBundleOf
import androidx.fragment.app.Fragment
import stream.nolambda.karma.bind

abstract class FragmentScreen : Fragment(), ContextProvider {

    abstract val binding: ScreenViewProvider<*>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.inflate(inflater, container)
    }

    override val ctx: Context
        get() = requireContext()
}