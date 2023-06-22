package com.folkmanis.laiks.data.domain

import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.model.LaiksUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class LaiksUserUseCase @Inject constructor(
    private val accountService: AccountService,
    private val laiksUserService: LaiksUserService,
) {

    operator fun invoke(): Flow<LaiksUser?> = accountService.firebaseUserFlow
        .flatMapLatest { user ->
            if (user == null) {
                flowOf(null)
            } else {
                laiksUserService.laiksUserFlow(user.uid)
            }
        }


}