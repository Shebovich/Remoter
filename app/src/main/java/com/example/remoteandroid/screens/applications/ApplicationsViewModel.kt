package com.example.remoteandroid.screens.applications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connectsdk.service.capability.Launcher
import com.example.remoteandroid.domain.models.ApplicationInfo
import com.example.remoteandroid.domain.models.ApplicationsState
import com.example.remoteandroid.domain.models.ConnectionState
import com.example.remoteandroid.domain.models.LaunchApplicationState
import com.example.remoteandroid.domain.usecase.GetApplicationsUseCase
import com.example.remoteandroid.domain.usecase.LaunchApplicationUseCase
import com.example.remoteandroid.screens.applications.mappers.ApplicationsContentUiMapper
import com.example.remoteandroid.screens.applications.models.ApplicationsContentViewState
import com.example.remoteandroid.screens.applications.models.ApplicationsNavigationState
import com.example.remoteandroid.screens.applications.models.ApplicationsPayload
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplicationsViewModel @Inject constructor(
    private val contentUiMapper: ApplicationsContentUiMapper,
    private val getApplicationsUseCase: GetApplicationsUseCase,
    private val launchApplicationUseCase: LaunchApplicationUseCase
) : ViewModel() {

    private val viewPayload = MutableStateFlow(ApplicationsPayload())

    val contentViewState: Flow<ApplicationsContentViewState> get() = _contentViewState
    private val _contentViewState = MutableStateFlow(currentContent)

    private val currentContent: ApplicationsContentViewState
        get() = contentUiMapper.toContent(viewPayload.value)

    private fun updateContent() {
        _contentViewState.value = currentContent
    }

    val navigationState: Flow<ApplicationsNavigationState> get() = _navigationState
    private val _navigationState = MutableSharedFlow<ApplicationsNavigationState>(replay = 0)

    fun onViewCreated() {
        updateContent()
    }

    fun onConnectionStateChanged(connectionState: ConnectionState) {
        println("ApplicationsViewModel: onConnectionStateChanged $connectionState")
        val state = getConnectedState(connectionState) ?: return
        viewModelScope.launch(Dispatchers.Main) {
            getApplicationsUseCase.invoke(state).collect {
                println("ApplicationsViewModel: getApplicationsUseCase $it")
                updatePayload(applicationsState = it)
            }
        }
    }

    private fun updatePayload(
        applicationsState: ApplicationsState = viewPayload.value.state,
    ) {
        viewPayload.update {
            it.copy(state = applicationsState)
        }
        updateContent()
    }

    private fun getConnectedState(connectionState: ConnectionState): ConnectionState.Connected? =
        (connectionState as? ConnectionState.Connected?)

    fun onApplicationClicked(applicationInfo: ApplicationInfo) {
        val connectionState = getConnectedState(viewPayload.value.connectionState) ?: return
        viewModelScope.launch {
            launchApplicationUseCase.invoke(connectionState.device, applicationInfo.id).collect {
                when(it){
                    LaunchApplicationState.SUCCESS -> _navigationState.tryEmit(ApplicationsNavigationState.TV_CONTROLS)
                    LaunchApplicationState.ERROR -> Unit
                }
            }
        }
    }
}