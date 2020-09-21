package ru.ppr.pprtest.adapters

import java.math.BigInteger

data class NumberItem(
    val number: BigInteger,
    val isWhiteColor: Boolean = true
)