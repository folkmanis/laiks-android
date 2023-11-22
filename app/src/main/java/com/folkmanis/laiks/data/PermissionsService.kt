package com.folkmanis.laiks.data

import kotlinx.coroutines.flow.Flow

interface PermissionsService {

    fun npBlockedFlow(uId: String): Flow<Boolean>

    suspend fun npBlocked(uId: String): Boolean

    fun isAdminFlow(uId: String): Flow<Boolean>

    suspend fun isAdmin(uId: String): Boolean

}