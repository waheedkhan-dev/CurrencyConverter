package com.codecollapse.currencyconverter.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.codecollapse.currencyconverter.data.repository.datastore.DataStoreRepositoryImpl
import com.codecollapse.currencyconverter.network.CurrencyApi
import com.codecollapse.currencyconverter.source.local.dao.ExchangeRateDao
import com.codecollapse.currencyconverter.utils.Constants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class ExchangeRateWorker @AssistedInject constructor(
    private val currencyApi: CurrencyApi,
    private val dataStoreRepository: DataStoreRepositoryImpl,
    private val exchangeRateDao: ExchangeRateDao,
    @Assisted val context: Context,
    @Assisted val parameters: WorkerParameters
) : CoroutineWorker(context, parameters) {
    companion object {
        private const val TAG = "ExchangeRateWorker"
    }

    override suspend fun doWork(): Result {

        val baseCurrency = dataStoreRepository.getBaseCurrency().getOrNull().orEmpty()

        val response = currencyApi.getLatestExchangeRates(Constants.API_KEY, baseCurrency)
        return if (response.isSuccessful) {
            val exchangeRate = response.body()
            exchangeRateDao.insertLatestExchangeRates(exchangeRate = exchangeRate!!)
            Timber.tag(TAG).d("Work-Done Successfully ")
            Result.success()
        } else {
            Timber.tag(TAG).d("Work-Fail Retry ")
            Result.retry()
        }
    }
}