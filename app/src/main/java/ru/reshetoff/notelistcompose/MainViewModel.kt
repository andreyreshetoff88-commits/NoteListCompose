package ru.reshetoff.notelistcompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.reshetoff.app_domain.usecase.HasAccessTokenUseCase
import ru.reshetoff.app_domain.usecase.SyncUseCase
import ru.reshetoff.app_domain.utils.Resource
import ru.reshetoff.common.State
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val hasAccessTokenUseCase: HasAccessTokenUseCase,
    private val syncUseCase: SyncUseCase
) : ViewModel() {

    private var _syncState = MutableStateFlow<State<Boolean>>(State.Empty())
    val syncState: StateFlow<State<Boolean>> get() = _syncState
    fun checkToken() = hasAccessTokenUseCase.execute()

    fun sync() {
        syncUseCase.execute().onEach { resource ->
            when (resource) {
                is Resource.Loading -> _syncState.value = State.Loading()
                is Resource.Success -> _syncState.value = State.Success(data = resource.data)
                is Resource.Error -> _syncState.value = State.Error(message = resource.message)
            }
        }.launchIn(viewModelScope)
    }
}