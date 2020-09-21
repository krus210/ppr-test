package ru.ppr.pprtest.repositories

import kotlin.math.floor
import kotlin.math.round
import kotlin.math.sqrt

object FibonacciRepository : AbstractRepository() {

    override suspend fun isRequiredNumber(number: Int): Boolean {
        return isPerfectSquare(5 * number * number + 4) ||
                isPerfectSquare(5 * number * number - 4)
    }

    private fun isPerfectSquare(x: Int): Boolean {
        val s = sqrt(x.toDouble())
        val f = floor(s)
        return s == f
    }
}