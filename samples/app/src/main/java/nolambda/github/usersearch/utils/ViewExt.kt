package nolambda.github.usersearch.utils

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.core.view.ViewCompat

fun View.setVisible(isVisible: Boolean, animate: Boolean = false) {
    if (animate) {
        if (isVisible) {
            alpha = 0f
            setVisible(true)
        }
        ViewCompat.animate(this)
            .alpha(if (isVisible) 1f else 0f)
            .withEndAction {
                if (!isVisible) {
                    setVisible(false)
                }
            }
    } else {
        visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}

fun EditText.onTextChange(debounceTime: Long = 0, block: (String) -> Unit) {
    val useDebounce = debounceTime > 0
    val handler = Handler(Looper.getMainLooper())

    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (useDebounce) {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    block(s.toString())
                }, debounceTime)
            } else {
                block(s.toString())
            }

        }
    })
}
