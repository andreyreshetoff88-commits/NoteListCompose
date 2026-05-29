package ru.reshetoff.notelistcompose.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.reshetoff.app_domain.usecase.HasAccessTokenUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val hasAccessTokenUseCase: HasAccessTokenUseCase
) : ViewModel() {
    fun checkToken() = hasAccessTokenUseCase.execute()
}