package ru.ppr.pprtest.viewmodels

import ru.ppr.pprtest.repositories.PrimeNumbersRepository

class PrimeNumbersViewModel: BaseViewModel() {

    override val repository = PrimeNumbersRepository
    override val numbersLiveData = repository.numbersLiveData
}
