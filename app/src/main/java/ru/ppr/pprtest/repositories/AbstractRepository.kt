package ru.ppr.pprtest.repositories

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.ppr.pprtest.adapters.NumberItem
import java.util.concurrent.atomic.AtomicInteger

abstract class AbstractRepository {

    val numbersLiveData = MutableLiveData<List<NumberItem>>()
    private val countForLoading = 50

    abstract suspend fun isRequiredNumber(number: Int): Boolean

    fun loadNumbers(
        lastItem: NumberItem? = null,
        preLastItem: NumberItem? = null
    ) {
        GlobalScope.launch {
            val firstNumber = if (lastItem == null) 0
            else lastItem.number + 1
            val asyncNumbers = async(Dispatchers.IO) {
                calculateNumbers(firstNumber)
            }
            val numbers = asyncNumbers.await()
            val listOfNumberItems = if (lastItem == null) mutableListOf()
            else mutableListOf(preLastItem!!, lastItem)
            var index = 0
            while (index <= numbers.lastIndex) {
                val number = NumberItem(numbers[index], isWhiteColor(listOfNumberItems))
                listOfNumberItems.add(number)
                index += 1
            }
            if (lastItem != null) {
                listOfNumberItems.removeFirst()
                listOfNumberItems.removeFirst()
            }
            withContext(Dispatchers.Main) {
                numbersLiveData.value = listOfNumberItems
            }
        }
    }

    private suspend fun calculateNumbers(firstNumber: Int): List<Int> {
        val numbers = mutableListOf<Int>()
        var number = firstNumber
        while (numbers.size < countForLoading) {
            if (isRequiredNumber(number)) numbers.add(number)
            number++
        }
        return numbers.sorted()
    }

    private fun isWhiteColor(numbers: List<NumberItem>): Boolean {
        val lastIndex = numbers.lastIndex
        return when {
            lastIndex == -1 -> false
            lastIndex == 0 -> true
            numbers[lastIndex].isWhiteColor == numbers[lastIndex - 1].isWhiteColor -> !numbers[lastIndex].isWhiteColor
            else -> numbers[lastIndex].isWhiteColor
        }
    }
}