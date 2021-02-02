package nolambda.github.usersearch.playground

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData

object Logger {

    private val subject = MutableLiveData<String>()
    private val loggerBuilder = StringBuilder()

    private val logText: String
        get() = loggerBuilder.toString()

    fun clear() {
        loggerBuilder.setLength(0)
    }

    fun divider() {
        log("-----------")
    }

    fun log(o: Any?) {
        val text = o?.toString() ?: "Object null"
        println(text)
        loggerBuilder.append(text).append("\n")
        subject.postValue(loggerBuilder.toString())
    }

    fun getLogView(context: Context): ViewGroup {
        val padding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 16f, context.resources.displayMetrics
        ).toInt()

        val textView = TextView(context)
        textView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        textView.setTextColor(Color.BLACK)
        textView.setPadding(padding, padding / 2, padding, padding / 2)

        val owner = context as AppCompatActivity
        subject.observe(owner) {
            textView.text = null
            textView.text = it
        }

        val scrollView = ScrollView(context)
        scrollView.addView(textView)
        return scrollView
    }
}
