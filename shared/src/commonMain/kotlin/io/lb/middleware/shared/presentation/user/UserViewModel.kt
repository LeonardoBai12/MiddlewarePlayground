package io.lb.middleware.shared.presentation.user

import io.lb.middleware.common.state.Resource
import io.lb.middleware.common.state.toCommonFlow
import io.lb.middleware.common.state.toCommonStateFlow
import io.middleware.user.domain.use_cases.DeleteUserUseCase
import io.middleware.user.domain.use_cases.GetCurrentUserUseCase
import io.middleware.user.domain.use_cases.LogoutUseCase
import io.middleware.user.domain.use_cases.UpdatePasswordUseCase
import io.middleware.user.domain.use_cases.UpdateUserUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(
    coroutineScope: CoroutineScope?,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val logoutUseCase: LogoutUseCase
) {
    sealed class UiEvent {
        data class ShowError(val message: String) : UiEvent()
        data object DisableUserTextFields : UiEvent()
        data object DisablePasswordTextFields : UiEvent()
        data object ShowUpdatedMessage : UiEvent()
        data object ShowLoggedOutMessage : UiEvent()
        data object ShowDeletedMessage : UiEvent()
    }

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(UserState())
    val state = _state.toCommonStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow().toCommonFlow()

    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.GetCurrentUser -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(isLoading = true)
                    }
                    val userData = getCurrentUserUseCase.invoke()
                    _state.update {
                        it.copy(
                            userData = userData,
                            userName = userData?.userName ?: "",
                            phone = userData?.phone ?: "",
                            email = userData?.email ?: "",
                            isLoading = false
                        )
                    }
                }
            }

            is UserEvent.UpdateUser -> {
                viewModelScope.launch {
                    updateUserUseCase.invoke(
                        userName = event.userName,
                        phone = event.phone,
                        email = event.email,
                        profilePictureUrl = event.profilePictureUrl,
                        password = event.password
                    ).collect {
                        when (it) {
                            is Resource.Success -> {
                                _state.update {
                                    it.copy(
                                        userData = it.userData?.copy(
                                            userName = event.userName,
                                            phone = event.phone,
                                            email = event.email,
                                            profilePictureUrl = event.profilePictureUrl
                                        )
                                    )
                                }
                                _eventFlow.emit(UiEvent.ShowUpdatedMessage)
                                _eventFlow.emit(UiEvent.DisableUserTextFields)
                            }

                            is Resource.Error -> {
                                _eventFlow.emit(UiEvent.ShowError(it.throwable?.message ?: ""))
                            }
                        }
                    }
                }
            }

            is UserEvent.UpdatePassword -> {
                viewModelScope.launch {
                    updatePasswordUseCase.invoke(
                        event.password,
                        event.repeatedPassword,
                        event.newPassword
                    ).collect {
                        when (it) {
                            is Resource.Success -> {
                                _eventFlow.emit(UiEvent.ShowUpdatedMessage)
                                _eventFlow.emit(UiEvent.DisablePasswordTextFields)
                            }

                            is Resource.Error -> {
                                _eventFlow.emit(UiEvent.ShowError(it.throwable?.message ?: ""))
                            }
                        }
                    }
                }
            }

            is UserEvent.DeleteUser -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(isLoading = true)
                    }
                    deleteUserUseCase.invoke(event.password).collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                _state.update {
                                    it.copy(
                                        userData = null,
                                        isLoading = false,
                                    )
                                }
                                _eventFlow.emit(UiEvent.ShowDeletedMessage)
                            }

                            is Resource.Error -> {
                                _state.update { it.copy(isLoading = false) }
                                _eventFlow.emit(UiEvent.ShowError(result.throwable?.message ?: ""))
                            }
                        }
                    }
                }
            }

            is UserEvent.Logout -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    logoutUseCase.invoke().collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                _state.update {
                                    it.copy(
                                        userData = null,
                                        isLoading = false,
                                    )
                                }
                                _eventFlow.emit(UiEvent.ShowLoggedOutMessage)
                            }

                            is Resource.Error -> {
                                _state.update { it.copy(isLoading = false) }
                                _eventFlow.emit(UiEvent.ShowError(result.throwable?.message ?: ""))
                            }
                        }
                    }
                }
            }

            UserEvent.ResetUserState -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(isLoading = true)
                    }
                    val userData = getCurrentUserUseCase.invoke()
                    _state.update {
                        it.copy(
                            userData = userData,
                            userName = userData?.userName ?: "",
                            phone = userData?.phone ?: "",
                            email = userData?.email ?: "",
                            isLoading = false
                        )
                    }
                    _eventFlow.emit(UiEvent.DisableUserTextFields)
                    _eventFlow.emit(UiEvent.DisablePasswordTextFields)
                }
            }
        }
    }
}
