package io.lb.middleware.shared.presentation.sign_up

import io.lb.middleware.common.shared.user.UserData
import io.lb.middleware.common.state.Resource
import io.lb.middleware.common.state.toCommonFlow
import io.middleware.sign_up.domain.use_cases.LoginUseCase
import io.middleware.sign_up.domain.use_cases.SignUpUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class SignUpViewModel(
    coroutineScope: CoroutineScope?,
    private val signUpUseCase: SignUpUseCase,
    private val loginUseCase: LoginUseCase
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow().toCommonFlow()

    sealed class UiEvent {
        data class ShowError(val message: String) : UiEvent()
        data class ShowLoginSuccess(val userData: UserData) : UiEvent()
        data class ShowSignUpSuccess(val userData: UserData) : UiEvent()
    }

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.RequestLogin -> {
                viewModelScope.launch {
                    loginUseCase.invoke(
                        email = event.email,
                        password = event.password
                    ).collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                result.data?.let { userData ->
                                    _eventFlow.emit(UiEvent.ShowLoginSuccess(userData))
                                } ?: run {
                                    _eventFlow.emit(UiEvent.ShowError("Could not login"))
                                }
                            }
                            is Resource.Error -> {
                                _eventFlow.emit(
                                    UiEvent.ShowError(
                                        result.throwable?.message ?: "Something went wrong"
                                    )
                                )
                            }
                        }
                    }
                }
            }
            is SignUpEvent.RequestSignUp -> {
                viewModelScope.launch {
                    signUpUseCase.invoke(
                        userName = event.userName,
                        phone = event.phone,
                        email = event.email,
                        profilePictureUrl = event.profilePictureUrl,
                        password = event.password,
                        repeatedPassword = event.repeatedPassword
                    ).collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                result.data?.let { userData ->
                                    _eventFlow.emit(UiEvent.ShowSignUpSuccess(userData))
                                } ?: run {
                                    _eventFlow.emit(UiEvent.ShowError("Could not sign up"))
                                }
                            }
                            is Resource.Error -> {
                                _eventFlow.emit(
                                    UiEvent.ShowError(
                                        result.throwable?.message ?: "Something went wrong"
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
