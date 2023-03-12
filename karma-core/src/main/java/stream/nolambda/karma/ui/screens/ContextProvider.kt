package stream.nolambda.karma.ui.screens

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding

interface ContextProvider {
    val ctx: Context
}

fun ContextProvider.resDrawable(@DrawableRes resId: Int): Drawable? =
    ContextCompat.getDrawable(ctx, resId)

fun ContextProvider.resColor(@ColorRes resId: Int): Int =
    ContextCompat.getColor(ctx, resId)

fun ContextProvider.xml(@LayoutRes resId: Int): ScreenViewProvider<View> =
    XmlScreenViewProvider(resId)

fun <T : ViewBinding> ContextProvider.viewBinding(creator: ViewBindingInflater<T>) =
    ViewBindingScreenViewProvider(creator)

typealias ViewBindingInflater<T> = (LayoutInflater, ViewGroup?, Boolean) -> T
