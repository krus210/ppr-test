package ru.ppr.pprtest.repositories

object PrimeNumbersRepository : AbstractRepository() {

    override suspend fun isRequiredNumber(number: Int): Boolean {
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