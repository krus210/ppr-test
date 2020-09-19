package ru.ppr.pprtest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.ppr.pprtest.adapters.NumberItem
import ru.ppr.pprtest.repositories.FibonacciRepository
import ru.ppr.pprtest.repositories.PrimeNumbersRepository

class NumbersViewModel(type: String): ViewModel() {

    private val repository = if (type == NumbersFragment.FIBONACCI_NUMBERS_TYPE) FibonacciRepository
    else PrimeNumbersRepository
    private val numbersLiveData = repository.numbersLiveData

    fun loadNumbers(
        lastItem: NumberItem? = null,
        preLastItem: NumberItem? = null
    ) {
        repository.loadNumbers(lastItem, preLastItem)
    }

    fun getNumbers() = numbersLiveData
}

class ViewModelFactory(private val params: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NumbersViewModel::class.java)) {
            return NumbersViewModel(params) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}