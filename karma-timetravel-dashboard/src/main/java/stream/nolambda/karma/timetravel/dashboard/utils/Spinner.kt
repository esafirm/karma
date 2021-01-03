package stream.nolambda.karma.timetravel.dashboard.utils

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