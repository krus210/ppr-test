package ru.ppr.pprtest.viewmodels

import ru.ppr.pprtest.repositories.FibonacciRepository

class FibonacciViewModel: BaseViewModel() {

    override val repository = FibonacciRepository
    override val numbersLiveData = repository.numbersLiveData
}