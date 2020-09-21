package ru.ppr.pprtest.repositories

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.ppr.pprtest.adapters.NumberItem
import java.math.BigInteger

object FibonacciRepository : AbstractRepository() {
    private val numbers = mutableListOf<BigInteger>()
    private val mutex = Mutex()

    init {
        GlobalScope.launch(Dispatchers.IO) {
            calculateNumbers()
        }
    }

    override fun loadNumbers(lastIndex: Int, lastItem: NumberItem?, preLastItem: NumberItem?) {
        GlobalScope.launch {
            val firstIndex = if (lastItem == null) 0 else lastIndex + 1
            if (numbers.size <= firstIndex + countForLoading + 1) {
                delay(100)
            }
            val subNumbers = numbers.subList(firstIndex, firstIndex + countForLoading)
            val listOfNumberItems = if (lastItem == null) mutableListOf()
            else mutableListOf(preLastItem!!, lastItem)
            var index = 0
            while (index <= subNumbers.lastIndex) {
                val number = NumberItem(subNumbers[index], isWhiteColor(listOfNumberItems))
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

    private suspend fun calculateNumbers() {
        val limit = 10000
        var t1 = BigInteger.valueOf(0L)
        var t2 = BigInteger.valueOf(1L)
        for (i in 1..limit) {
            mutex.withLock {
                numbers.add(t1)
            }
            val sum = t1.add(t2)
            t1 = t2
            t2 = sum
        }
    }
}