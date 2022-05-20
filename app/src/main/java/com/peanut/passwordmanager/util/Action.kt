package com.peanut.passwordmanager.util

import com.peanut.passwordmanager.R

enum class Action(val actionNameStringResourceId: Int) {
    ADD(R.string.action_add),
    UPDATE(R.string.action_update),
    DELETE(R.string.action_delete),
    UNDO(R.string.action_undo),
    NO_ACTION(R.string.action_no_action)
}

fun String?.toAction(): Action{
    return when(this){
        "ADD" -> Action.ADD
        "UPDATE" -> Action.UPDATE
        "DELETE" -> Action.DELETE
        "UNDO" -> Action.UNDO
        else -> Action.NO_ACTION
    }
}