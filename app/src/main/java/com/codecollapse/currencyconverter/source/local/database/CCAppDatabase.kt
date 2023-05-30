package com.codecollapse.currencyconverter.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.codecollapse.currencyconverter.data.model.currency.Currency
import com.codecollapse.currencyconverter.data.model.exchangeRate.ExchangeRate
import com.codecollapse.currencyconverter.source.local.dao.CurrencyDao
import com.codecollapse.currencyconverter.source.local.dao.ExchangeRateDao
import com.codecollapse.currencyconverter.utils.Constants
import com.codecollapse.currencyconverter.utils.converter.ExchangeRateConverter

@Database(
    entities = [ExchangeRate::class, Currency::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ExchangeRateConverter::class)
abstract class CCAppDatabase : RoomDatabase() {

    abstract fun exchangeRateDao(): ExchangeRateDao
    abstract fun currencyDao(): CurrencyDao

    companion object {
        const val DATABASE_NAME = Constants.APP_DATABASE
    }

    /* fun deleteTables() {
         userDao().deleteUser()
         chatMessagesDao().deleteChatMessages()
         inboxDao().deleteInbox()
         recordingSettingDao().deleteRecordingSetting()
     }*/
}