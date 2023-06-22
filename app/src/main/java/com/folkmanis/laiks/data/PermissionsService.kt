package com.folkmanis.laiks.data

import com.folkmanis.laiks.model.Permissions
import kotlinx.coroutines.flow.Flow

interface PermissionsService {

    suspend fun getPermissions(uId: String): Permissions

    fun getPermissionFlow(uId: String, key: String): Flow<Boolean>

    suspend fun updatePermission(uId: String, field: String, value: Any)

}