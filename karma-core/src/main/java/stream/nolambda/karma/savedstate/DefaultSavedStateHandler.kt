package stream.nolambda.karma.savedstate

import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Size
import android.util.SizeF
import android.util.SparseArray
import androidx.lifecycle.SavedStateHandle
import java.io.Serializable
import java.util.*

class DefaultSavedStateHandler<STATE> : SavedStateHandler<STATE> {

    private lateinit var savedState: SavedStateHandle
    private var lastState: STATE? = null

    override fun init(savedStateHandle: SavedStateHandle) {
        savedState = savedStateHandle
        lastState = savedStateHandle[KEY_STATE]
    }

    override fun saveToSavedState(state: STATE) {
        if (isValidState(state)) {
            savedState[KEY_STATE] = state
        }
    }

    override fun getSavedState(): STATE? = lastState

    private fun isValidState(value: Any?): Boolean {
        if (value == null) {
            return false
        }

        for (cl in ACCEPTABLE_CLASSES) {
            if (cl?.isInstance(value) == true) {
                return true
            }
        }
        return false
    }

    companion object {
        private const val KEY_STATE = "DSSH.State"

        private val ACCEPTABLE_CLASSES = arrayOf( //baseBundle
            Boolean::class.javaPrimitiveType,
            BooleanArray::class.java,
            Double::class.javaPrimitiveType,
            DoubleArray::class.java,
            Int::class.javaPrimitiveType,
            IntArray::class.java,
            Long::class.javaPrimitiveType,
            LongArray::class.java,
            String::class.java,
            Array<String>::class.java,  //bundle
            Binder::class.java,
            Bundle::class.java,
            Byte::class.javaPrimitiveType,
            ByteArray::class.java,
            Char::class.javaPrimitiveType,
            CharArray::class.java,
            CharSequence::class.java,
            Array<CharSequence>::class.java,  // type erasure ¯\_(ツ)_/¯, we won't eagerly check elements contents
            ArrayList::class.java,
            Float::class.javaPrimitiveType,
            FloatArray::class.java,
            Parcelable::class.java,
            Array<Parcelable>::class.java,
            Serializable::class.java,
            Short::class.javaPrimitiveType,
            ShortArray::class.java,
            SparseArray::class.java,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) Size::class.java else Int::class.javaPrimitiveType,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) SizeF::class.java else Int::class.javaPrimitiveType
        )
    }
}