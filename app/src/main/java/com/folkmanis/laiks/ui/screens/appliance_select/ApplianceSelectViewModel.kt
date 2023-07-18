package com.folkmanis.laiks.ui.screens.appliance_select

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.folkmanis.laiks.data.AccountService
import com.folkmanis.laiks.data.AppliancesService
import com.folkmanis.laiks.data.LaiksUserService
import com.folkmanis.laiks.data.domain.LaiksUserUseCase
import com.folkmanis.laiks.model.ApplianceRecord
import com.folkmanis.laiks.model.ApplianceType
import com.folkmanis.laiks.model.PowerAppliance
import com.folkmanis.laiks.model.PowerApplianceRecord
import com.folkmanis.laiks.utilities.NotLoggedInException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

fun List<ApplianceRecord>.filterTypeIds(type: Int): List<String> =
    filter { it.type == type }
        .map { it.applianceId }

@HiltViewModel
class ApplianceSelectViewModel @Inject constructor(
    private val appliancesService: AppliancesService,
    private val laiksUserService: LaiksUserService,
    private val accountService: AccountService,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ApplianceSelectUiState>(ApplianceSelectUiState.Loading)
    val uiState = _uiState.asStateFlow()

    suspend fun initialize() {
        try {
            getAppliances()
        } catch (e: NotLoggedInException) {
            _uiState.value = ApplianceSelectUiState.Error(e)
        }
    }

    val selectedValue: PowerApplianceRecord?
        get() {
            val state = _uiState.value
            return if (state is ApplianceSelectUiState.Success) {
                state.selected
            } else {
                null
            }
        }

    fun selectAppliance(record: PowerApplianceRecord?) {
        _uiState.update { state ->
            if (state is ApplianceSelectUiState.Success) {
                state.copy(selected = record)
            } else {
                _uiState.value
            }
        }
    }

    private suspend fun getAppliances() {

        val user = accountService.authUser ?: throw NotLoggedInException()
        val laiksUser = laiksUserService.laiksUser(user.uid) ?: throw NotLoggedInException()

        val activeUserAppliancesIds = laiksUser.appliances.filterTypeIds(ApplianceType.USER.type)
        val userAppliances = laiksUserService.userAppliances(user.uid).filter { appliance ->
            !activeUserAppliancesIds.contains(appliance.id)
        }

        val activeSystemAppliancesIds =
            laiksUser.appliances.filterTypeIds(ApplianceType.SYSTEM.type)
        val systemAppliances = appliancesService.activeAppliances().filter { appliance ->
            !activeSystemAppliancesIds.contains(appliance.id)
        }

        _uiState.value = ApplianceSelectUiState.Success(
            userAppliances = userAppliances,
            systemAppliances = systemAppliances,
        )
    }
}
