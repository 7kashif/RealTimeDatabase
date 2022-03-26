package com.kashif.realtimedatabase

data class NoteItem(
    var itemId: Long = 0L,
    var itemTitle: String = "",
    var itemBody: String = "",
    var done: Boolean = false
)
