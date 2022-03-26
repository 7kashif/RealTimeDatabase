package com.kashif.realtimedatabase

import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun formatDay(time: Long): String {
        val formatter = SimpleDateFormat("d MMM h:mm a", Locale.getDefault())
        return formatter.format(Date(time))
    }
}