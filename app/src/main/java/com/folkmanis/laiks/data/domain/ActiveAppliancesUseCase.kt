package com.folkmanis.laiks.data.domain

import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.AppliancesService
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ActiveAppliancesUseCase @Inject constructor(
    accountService: AccountService,
    appliancesService: AppliancesService,
) {

//   val applianceRecords = accountService.laiksUserFlow
//       .map { user-> user?.appliances ?: emptyList() }
}