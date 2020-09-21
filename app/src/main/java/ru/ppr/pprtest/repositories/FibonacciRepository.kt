package ru.ppr.pprtest.repositories

import java.math.BigInteger
import java.math.*
import java.math.BigDecimal
import kotlin.math.floor
import kotlin.math.round
import kotlin.math.sqrt

object FibonacciRepository : AbstractRepository() {

    override suspend fun isRequiredNumber(number: Int): Boolean {
        val bigIntegerOfNumber = BigInteger.valueOf(number.toLong())
        val bigInteger1 = BigInteger.valueOf(5)
            .multiply(bigIntegerOfNumber)
            .multiply(bigIntegerOfNumber)
        val bigInteger2 = BigInteger.valueOf(4)
        return isPerfectSquare(bigInteger1.plus(bigInteger2)) ||
                isPerfectSquare(bigInteger1.minus(bigInteger2))
    }

    private fun isPerfectSquare(value: BigInteger): Boolean {
        val square = BigDecimal(sqrt(value.toDouble())).toBigInteger()
        return square.multiply(square) == value
    }
}