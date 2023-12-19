package com.folkmanis.laiks.data.domain

import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.PermissionsService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class NpBlockedUseCase @Inject constructor(
    private val permissionsService: PermissionsService,
    private val accountService: AccountService,
) {
    operator fun invoke(): Flow<Boolean> {
       return accountService.firebaseUserFlow
            .flatMapLatest { user ->
                user?.uid?.let {
                    permissionsService.npBlockedFlow(it)
                } ?: flowOf(true)
            }
    }
}