package com.codecollapse.currencyconverter.source.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.codecollapse.currencyconverter.source.datastore.DataStoreRepository.PreferencesKey.DEFAULT_BASE_CURRENCY
import com.codecollapse.currencyconverter.source.datastore.DataStoreRepository.PreferencesKey.DEFAULT_TARGET_CURRENCY
import com.codecollapse.currencyconverter.source.datastore.DataStoreRepository.PreferencesKey.IS_DEVICE_SYNC_WITH_API
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepository @Inject constructor(private val context: Context) {

    object PreferencesKey {
        val DEFAULT_BASE_CURRENCY = stringPreferencesKey("defaultBaseCurrency")
        val DEFAULT_TARGET_CURRENCY = stringPreferencesKey("defaultTargetCurrency")
        val IS_DEVICE_SYNC_WITH_API = booleanPreferencesKey("isDeviceSyncWithAPI")
    }

    private val Context.dataStore by preferencesDataStore(name = "CurrencyConverterPreferences")

    suspend fun setBaseCurrency(baseCurrency: String) {
        context.dataStore.edit { pref ->
            pref[DEFAULT_BASE_CURRENCY] = baseCurrency
        }
    }

    suspend fun setTargetCurrency(targetCurrency: String) {
        context.dataStore.edit { pref ->
            pref[DEFAULT_TARGET_CURRENCY] = targetCurrency
        }
    }

    suspend fun isDeviceSync(isDeviceSync: Boolean) {
        context.dataStore.edit { pref ->
            pref[IS_DEVICE_SYNC_WITH_API] = isDeviceSync
        }
    }


    val getBaseCurrency: Flow<String> = context.dataStore.data
        .catch {
            if (this is Exception) {
                emit(emptyPreferences())
            }
        }.map { preference ->
            val name = preference[DEFAULT_BASE_CURRENCY] ?: "PKR"
            name
        }

    val getTargetCurrency: Flow<String> = context.dataStore.data
        .catch {
            if (this is Exception) {
                emit(emptyPreferences())
            }
        }.map { preference ->
            val name = preference[DEFAULT_TARGET_CURRENCY] ?: "USD"
            name
        }

    val getIsDeviceSync: Flow<Boolean> = context.dataStore.data
        .catch {
            if (this is Exception) {
                emit(emptyPreferences())
            }
        }.map { preference ->
            val name = preference[IS_DEVICE_SYNC_WITH_API] ?: false
            name
        }
}