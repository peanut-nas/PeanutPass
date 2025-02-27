package com.peanut.passwordmanager.service

import android.app.assist.AssistStructure
import android.app.assist.AssistStructure.ViewNode
import android.view.autofill.AutofillId

class WindowNodeFinder(private val cache: MutableMap<String, ViewNode?> = mutableMapOf()) {
    fun find(key: String, structure: AssistStructure): AutofillId? {
        return cache.getOrPut(key) {
            findKey(key, structure)
        }?.autofillId
    }

    fun getNode(key: String, structure: AssistStructure): ViewNode? {
        return findKey(key, structure)
    }

    private fun findKey(key: String, structure: AssistStructure): ViewNode? {
        for (i in 0 until structure.windowNodeCount) {
            val f = traverseViewNode(structure.getWindowNodeAt(i).rootViewNode) { node ->
                //match
                if (node.idEntry?.contains(key) == true){
                    return@traverseViewNode node
                }
                return@traverseViewNode null
            }
            if (f != null) return f
        }
        return null
    }

    private fun AssistStructure.forEachWindowNode(action: (ViewNode) -> Unit) {
        for (i in 0 until windowNodeCount) {
            action(this.getWindowNodeAt(i).rootViewNode)
        }
    }

    private fun traverseViewNode(node: ViewNode, visitor: (ViewNode) -> ViewNode?): ViewNode? {
        val r = visitor(node)
        if (r != null) return r
        for (i in 0 until node.childCount) {
            val r1 = traverseViewNode(node.getChildAt(i), visitor)
            if (r1 != null) return r1
        }
        return null
    }

}