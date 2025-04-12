import Shared

//
//  IOSSignUpViewModel.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

extension LoginScreen {
    @MainActor class IOSSignUpViewModel: ObservableObject {
        private var signUpUseCase: Sign_up_domainSignUpUseCase
        private var loginUseCase: Sign_up_domainLoginUseCase
        
        private let viewModel: SignUpViewModel
        
        @Published
        var state: SignUpState = SignUpState(
            isLoading: false
        )
        
        @Published
        var eventFlow: StateCommonFlow<SignUpViewModel.UiEvent>

        private var handler: StateDisposableHandle?
        
        init(
            signUpUseCase: Sign_up_domainSignUpUseCase,
            loginUseCase: Sign_up_domainLoginUseCase
        ) {
            self.signUpUseCase = signUpUseCase
            self.loginUseCase = loginUseCase
            self.viewModel = SignUpViewModel(
                coroutineScope: nil,
                signUpUseCase: signUpUseCase,
                loginUseCase: loginUseCase
            )
            self.eventFlow = viewModel.eventFlow
        }
        
        func onEvent(event: SignUpEvent) {
            self.viewModel.onEvent(event: event)
        }
        
        func startObserving() {
            handler = viewModel.state.subscribe(
                onCollect: { state in
                    if let state = state {
                        self.state = state
                    }
                }
            )
        }
        
        func dispose() {
            handler?.dispose()
        }
    }
}

extension SignInScreen {
    @MainActor class IOSSignUpViewModel: ObservableObject {
        private var signUpUseCase: Sign_up_domainSignUpUseCase
        private var loginUseCase: Sign_up_domainLoginUseCase
        
        private let viewModel: SignUpViewModel
        
        @Published
        var state: SignUpState = SignUpState(
            isLoading: false
        )
        
        @Published
        var eventFlow: StateCommonFlow<SignUpViewModel.UiEvent>

        private var handler: StateDisposableHandle?
        
        init(
            signUpUseCase: Sign_up_domainSignUpUseCase,
            loginUseCase: Sign_up_domainLoginUseCase
        ) {
            self.signUpUseCase = signUpUseCase
            self.loginUseCase = loginUseCase
            self.viewModel = SignUpViewModel(
                coroutineScope: nil,
                signUpUseCase: signUpUseCase,
                loginUseCase: loginUseCase
            )
            self.eventFlow = viewModel.eventFlow
        }
        
        func onEvent(event: SignUpEvent) {
            self.viewModel.onEvent(event: event)
        }
        
        func startObserving() {
            handler = viewModel.state.subscribe(
                onCollect: { state in
                    if let state = state {
                        self.state = state
                    }
                }
            )
        }
        
        func dispose() {
            handler?.dispose()
        }
    }
}
