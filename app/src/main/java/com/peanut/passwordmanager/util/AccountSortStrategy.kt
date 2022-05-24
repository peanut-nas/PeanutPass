package com.peanut.passwordmanager.util

import com.peanut.passwordmanager.R

enum class AccountSortStrategy(val sortStrategyStringResourceId: Int) {
    LastRecentUsed(R.string.account_sort_LRU),
    LastFrequentUsed(R.string.account_sort_LFU),
    LastCreated(R.string.account_sort_LC)
}