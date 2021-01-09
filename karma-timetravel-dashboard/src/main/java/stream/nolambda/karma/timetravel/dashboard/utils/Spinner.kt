package stream.nolambda.karma.timetravel.dashboard.utils

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

fun <T> Spinner.attach(
    data: List<T>,
    dropdown: Int = android.R.layout.simple_spinner_dropdown_item,
    listItem: Int = android.R.layout.simple_spinner_item
) {
    val adapter = ArrayAdapter(context, listItem, data)
    adapter.setDropDownViewResource(dropdown)
    this.adapter = adapter
}

fun Spinner.setOnItemSelected(onItemSelected: (Int) -> Unit) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            onItemSelected.invoke(p2)
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
        }
    }
}