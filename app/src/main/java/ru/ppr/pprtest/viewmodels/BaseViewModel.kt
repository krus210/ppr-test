package ru.ppr.pprtest.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.ppr.pprtest.NumbersFragment
import ru.ppr.pprtest.adapters.NumberItem
import ru.ppr.pprtest.repositories.AbstractRepository
import ru.ppr.pprtest.repositories.FibonacciRepository
import ru.ppr.pprtest.repositories.PrimeNumbersRepository

abstract class BaseViewModel: ViewModel() {
    abstract val repository: AbstractRepository
    abstract val numbersLiveData: MutableLiveData<List<NumberItem>>

    fun loadNumbers(
        lastIndex: Int = 0,
        lastItem: NumberItem?= null,
        preLastItem: NumberItem?=null
    ) {
        repository.loadNumbers(lastIndex, lastItem, preLastItem)
    }

    fun getNumbers() = numbersLiveData
}

class ViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrimeNumbersViewModel::class.java)) {
            return PrimeNumbersViewModel() as T
        }
        if (modelClass.isAssignableFrom(FibonacciViewModel::class.java)) {
            return FibonacciViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}