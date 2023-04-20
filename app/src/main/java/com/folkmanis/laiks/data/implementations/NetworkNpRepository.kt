package com.folkmanis.laiks.data.implementations

import android.util.Log
import com.folkmanis.laiks.data.NpRepository
import com.folkmanis.laiks.data.network.NpApiService
import com.folkmanis.laiks.model.np_data.NpServerData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NetworkNpRepository @Inject constructor(
    private val npApiService: NpApiService,
) : NpRepository {
    override suspend fun getNpData(): NpServerData {

        val npData = withContext(Dispatchers.IO) {
            npApiService.getNpData()
        }
        Log.d(TAG, "Retrieved ${npData.data.rows.size} rows")
        return npData
    }

    companion object {
        const val TAG = "NetworkNpRepository"
    }
}