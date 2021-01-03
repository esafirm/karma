package stream.nolambda.karma.ui.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding

interface ScreenViewProvider<T> {
    fun inflate(inflater: LayoutInflater, parent: ViewGroup?): View
    fun getBinding(): T
}

class ViewBindingScreenViewProvider<T : ViewBinding>(
    private val creator: ViewBindingInflater<T>
) : ScreenViewProvider<T> {

    private lateinit var binding: T

    override fun inflate(inflater: LayoutInflater, parent: ViewGroup?): View {
        binding = creator.invoke(inflater, parent, false)
        return binding.root
    }

    override fun getBinding(): T = binding
}

class XmlScreenViewProvider(
    @LayoutRes private val resId: Int
) : ScreenViewProvider<View> {

    private lateinit var view: View

    override fun getBinding(): View = view

    override fun inflate(inflater: LayoutInflater, parent: ViewGroup?): View {
        view = inflater.inflate(resId, parent, false)
        return view
    }
}