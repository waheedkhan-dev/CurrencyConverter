package com.codecollapse.currencyconverter.data.repository.datastore

interface DataStoreRepository {

    suspend fun setBaseCurrency(baseCurrency: String)
    suspend fun setTargetCurrency(targetCurrency: String)

    suspend fun setFromCountry(country : String)

    suspend fun setToCountry(country: String)

    suspend fun setAmount(amount: Int)

    suspend fun isDeviceSync(isDeviceSync: Boolean)

    suspend fun getBaseCurrency(): Result<String>

    suspend fun getTargetCurrency(): Result<String>
    suspend fun getFromCountry(): Result<String>
    suspend fun getToCountry(): Result<String>
    suspend fun getIsDeviceSync(): Result<Boolean>
    suspend fun getAmount(): Result<Int>

}

