package ru.ppr.pprtest.repositories

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import ru.ppr.pprtest.adapters.NumberItem
import java.math.BigInteger

abstract class AbstractRepository {

    val numbersLiveData = MutableLiveData<List<NumberItem>>()
    val countForLoading = 50

    abstract fun loadNumbers(lastIndex: Int, lastItem: NumberItem?, preLastItem: NumberItem?)

    fun isWhiteColor(numbers: List<NumberItem>): Boolean {
        val lastIndex = numbers.lastIndex
        return when {
            lastIndex == -1 -> false
            lastIndex == 0 -> true
            numbers[lastIndex].isWhiteColor == numbers[lastIndex - 1].isWhiteColor -> !numbers[lastIndex].isWhiteColor
            else -> numbers[lastIndex].isWhiteColor
        }
    }
}