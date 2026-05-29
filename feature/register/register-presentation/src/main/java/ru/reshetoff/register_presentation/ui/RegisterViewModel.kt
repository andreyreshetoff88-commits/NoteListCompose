package ru.reshetoff.register_presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import ru.reshetoff.common.State
import ru.reshetoff.register_domain.model.Country
import ru.reshetoff.register_domain.model.RegisterRequest
import ru.reshetoff.register_domain.usecase.GetCountryListUseCase
import ru.reshetoff.register_domain.usecase.RegisterUseCase
import ru.reshetoff.utils.Resource
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    getCountryListUseCase: GetCountryListUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private var _registerState = MutableStateFlow<State<Unit>>(State.Empty())
    val registerState: StateFlow<State<Unit>> get() = _registerState
    val countiesState: StateFlow<State<List<Country>>> =
        getCountryListUseCase.execute().map { resource ->
            when (resource) {
                is Resource.Loading -> State.Loading()
                is Resource.Success -> State.Success(data = resource.data)
                is Resource.Error -> State.Error(message = resource.message)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = State.Empty()
        )

    fun register(registerRequest: RegisterRequest) {
        registerUseCase.execute(registerRequest).onEach { resource ->
            when (resource) {
                is Resource.Loading -> _registerState.value = State.Loading()
                is Resource.Success -> _registerState.value = State.Success(Unit)
                is Resource.Error -> _registerState.value = State.Error(message = resource.message)
            }
        }.launchIn(viewModelScope)
    }
}