package com.peanut.passwordmanager.util

fun generatePassword(
    length: Int,
    useLetter: Boolean = false,
    useUpperLetter: Boolean = false,
    useNumber: Boolean = false,
    useSymbol: Boolean = false
): List<Pair<String, Int>> {
    if (!(useLetter || useUpperLetter || useNumber || useSymbol))
        return emptyList()
    val basic = "abcdefghijklmnopqrstuvwxyz"
    val upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val number = "0123456789"
    val symbol = "!@#$%^&*()_+-={}[]|:;'\"\\,<.>/?`~"
    val candidates =
        (if (useLetter) basic else "") + (if (useUpperLetter) upper else "") + (if (useNumber) number else "") + (if (useSymbol) symbol else "")
    val sb = mutableListOf<Pair<String, Int>>()
    for (i in 0 until length) {
        val selected = candidates[(Math.random() * candidates.length).toInt()]
        val type = if (selected in basic || selected in upper) 0 else {
            if (selected in number) 1 else 2
        }
        sb.add(selected.toString() to type)
    }
    return sb
}