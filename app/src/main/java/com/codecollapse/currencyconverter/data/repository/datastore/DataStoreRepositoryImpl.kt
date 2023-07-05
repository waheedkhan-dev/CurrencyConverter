package com.codecollapse.currencyconverter.data.repository.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.codecollapse.currencyconverter.data.repository.datastore.DataStoreRepositoryImpl.PreferencesKey.AMOUNT
import com.codecollapse.currencyconverter.data.repository.datastore.DataStoreRepositoryImpl.PreferencesKey.DEFAULT_BASE_CURRENCY
import com.codecollapse.currencyconverter.data.repository.datastore.DataStoreRepositoryImpl.PreferencesKey.DEFAULT_TARGET_CURRENCY
import com.codecollapse.currencyconverter.data.repository.datastore.DataStoreRepositoryImpl.PreferencesKey.IS_DEVICE_SYNC_WITH_API
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import okio.IOException
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(private val ccDataStore: DataStore<Preferences>) :
    DataStoreRepository {
    private object PreferencesKey {
        val DEFAULT_BASE_CURRENCY = stringPreferencesKey("defaultBaseCurrency")
        val DEFAULT_TARGET_CURRENCY = stringPreferencesKey("defaultTargetCurrency")
        val IS_DEVICE_SYNC_WITH_API = booleanPreferencesKey("isDeviceSyncWithAPI")
        val AMOUNT = intPreferencesKey("amount")
    }

    override suspend fun setBaseCurrency(baseCurrency: String) {
        ccDataStore.edit { pref ->
            pref[DEFAULT_BASE_CURRENCY] = baseCurrency
        }
    }

    override suspend fun setTargetCurrency(targetCurrency: String) {
        ccDataStore.edit { pref ->
            pref[DEFAULT_TARGET_CURRENCY] = targetCurrency
        }
    }

    override suspend fun setAmount(amount: Int) {
        ccDataStore.edit { pref ->
            pref[AMOUNT] = amount
        }
    }

    override suspend fun isDeviceSync(isDeviceSync: Boolean) {
        ccDataStore.edit { pref ->
            pref[IS_DEVICE_SYNC_WITH_API] = isDeviceSync
        }
    }

    override suspend fun getBaseCurrency(): Result<String> {
        return Result.runCatching {
            val flow = ccDataStore.data
                .catch { exception ->
                    /*
                     * dataStore.data throws an IOException when an error
                     * is encountered when reading data
                     */
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map { preferences ->
                    // Get our name value, defaulting to "" if not set
                    preferences[DEFAULT_BASE_CURRENCY]
                }
            val value = flow.firstOrNull() ?: "USD" // we only care about the 1st value
            value
        }
    }

    override suspend fun getTargetCurrency(): Result<String> {
        return Result.runCatching {
            val flow = ccDataStore.data
                .catch { exception ->
                    /*
                     * dataStore.data throws an IOException when an error
                     * is encountered when reading data
                     */
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map { preferences ->
                    preferences[DEFAULT_TARGET_CURRENCY]
                }
            val value = flow.firstOrNull() ?: "GBP"
            value
        }
    }

    override suspend fun getIsDeviceSync(): Result<Boolean> {
        return Result.runCatching {
            val flow = ccDataStore.data
                .catch { exception ->
                    /*
                     * dataStore.data throws an IOException when an error
                     * is encountered when reading data
                     */
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map { preferences ->
                    preferences[IS_DEVICE_SYNC_WITH_API]
                }
            val value = flow.firstOrNull() ?: false
            value
        }
    }

    override suspend fun getAmount(): Result<Int> {
        return Result.runCatching {
            val flow = ccDataStore.data
                .catch { exception ->
                    /*
                     * dataStore.data throws an IOException when an error
                     * is encountered when reading data
                     */
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map { preferences ->
                    preferences[AMOUNT]
                }
            val value = flow.firstOrNull() ?: 300
            value
        }
    }
}