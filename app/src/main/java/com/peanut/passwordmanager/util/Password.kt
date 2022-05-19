package com.peanut.passwordmanager.util

const val basic = "abcdefghijklmnopqrstuvwxyz"
const val upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
const val number = "0123456789"
const val symbol = "!@#$%^&*()_+-={}[]|:;'\"\\,<.>/?`~"

fun generatePassword(
    length: Int,
    useLetter: Boolean = false,
    useUpperLetter: Boolean = false,
    useNumber: Boolean = false,
    useSymbol: Boolean = false
): List<Pair<String, Int>> {
    if (!(useLetter || useUpperLetter || useNumber || useSymbol))
        return emptyList()
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

fun List<Pair<String, Int>>.clearType(): String{
    val sb = StringBuilder()
    for(p in this){
        sb.append(p.first)
    }
    return sb.toString()
}

fun guessTypeFromString(password: String): List<Pair<String, Int>>{
    val sb = mutableListOf<Pair<String, Int>>()
    for (i in password) {
        val type = if (i in basic || i in upper) 0 else if (i in number) 1 else if (i in symbol) 2 else 0
        sb.add(i.toString() to type)
    }
    return sb
}