package com.folkmanis.laiks.data.domain

import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.PermissionsService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class IsPermissionUseCase @Inject constructor(
    private val permissionsService: PermissionsService,
    private val accountService: AccountService,
    ) {

    operator fun invoke(permission: String): Flow<Boolean> =
        accountService.firebaseUserFlow
            .flatMapLatest { user ->
                if (user != null) {
                    permissionsService
                        .getPermissionFlow(user.uid, permission)
                } else {
                    flowOf(false)
                }
            }

}