package com.gulftechiinovations.product_scanner.data.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.gulftechiinovations.product_scanner.util.HttpConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DataStoreConstants.PREFERENCE_NAME)


class DataStoreServiceImpl(context: Context):DataStoreService {

    private object PreferenceKeys {
        val operationCountKey = intPreferencesKey(name = DataStoreConstants.OPERATION_COUNT_KEY)
        val baseUrlKey = stringPreferencesKey(name = DataStoreConstants.BASE_URL_KEY)
        val ipAddressKey = stringPreferencesKey(name = DataStoreConstants.IP_ADDRESS_KEY)
        val uniLicenseKey = stringPreferencesKey(name = DataStoreConstants.UNI_LICENSE_SAVE_KEY)
        val deviceIdKey = stringPreferencesKey(name = DataStoreConstants.DEVICE_ID_KEY)
    }

    private val dataStore = context.dataStore

    override suspend fun updateOperationCount() {
        dataStore.edit { preference ->
            val count = preference[PreferenceKeys.operationCountKey] ?: 0
            preference[PreferenceKeys.operationCountKey] = count + 1
        }
    }

    override suspend fun saveBaseUrl(baseUrl: String) {
        dataStore.edit { preference ->
            preference[PreferenceKeys.baseUrlKey] = baseUrl
        }
    }



    override suspend fun saveIpAddress(ipAddress: String) {
        dataStore.edit { preference ->
            preference[PreferenceKeys.ipAddressKey] = ipAddress
        }
    }



    override fun readOperationCount(): Flow<Int> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException)
                    emit(emptyPreferences())
                else
                    throw exception
            }
            .map { preferences ->
                val operationCount = preferences[PreferenceKeys.operationCountKey] ?: 0
                operationCount
            }
    }

    override fun readBaseUrl(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException)
                    emit(emptyPreferences())
                else
                    throw exception
            }
            .map { preferences ->
                val baseUrl = preferences[PreferenceKeys.baseUrlKey] ?: HttpConstants.BASE_URL
                baseUrl
            }
    }


    override fun readIpaddress(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException)
                    emit(emptyPreferences())
                else
                    throw exception
            }
            .map { preferences ->
                val ipAddress = preferences[PreferenceKeys.ipAddressKey] ?: ""
                ipAddress
            }
    }



    override suspend fun saveUniLicenseData(uniLicenseString: String) {
        dataStore.edit { preference ->
            preference[PreferenceKeys.uniLicenseKey] = uniLicenseString
        }
    }

    override suspend fun saveDeviceId(deviceId: String) {
        dataStore.edit { preference ->
            preference[PreferenceKeys.deviceIdKey] = deviceId
        }
    }

    override fun readUniLicenseData(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException)
                    emit(emptyPreferences())
                else
                    throw exception
            }
            .map { preferences ->
                val portAddress = preferences[PreferenceKeys.uniLicenseKey]?:""
                portAddress
            }
    }

    override fun readDeviceId(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException)
                    emit(emptyPreferences())
                else
                    throw exception
            }
            .map { preferences ->
                val deviceId = preferences[PreferenceKeys.deviceIdKey] ?: ""
                deviceId
            }
    }
}