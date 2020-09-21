package ru.ppr.pprtest.repositories

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.withLock
import ru.ppr.pprtest.adapters.NumberItem
import java.math.BigInteger

object PrimeNumbersRepository : AbstractRepository() {

    override fun loadNumbers(lastIndex: Int, lastItem: NumberItem?, preLastItem: NumberItem?) {
        GlobalScope.launch {
            val firstNumber = lastItem?.number?.toInt() ?: 0
            val asyncNumbers = GlobalScope.async(Dispatchers.IO) {
                calculateNumbers(firstNumber)
            }
            val numbers = asyncNumbers.await()
            val listOfNumberItems = if (lastItem == null) mutableListOf()
            else mutableListOf(preLastItem!!, lastItem)
            var index = 0
            while (index <= numbers.lastIndex) {
                val number = NumberItem(numbers[index].toBigInteger(), isWhiteColor(listOfNumberItems))
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

    private suspend fun isRequiredNumber(number: Int): Boolean {
        var flag = false
        for (i in 2..number / 2) {
            if (number % i == 0) {
                flag = true
                break
            }
        }
        return !flag
    }

}

