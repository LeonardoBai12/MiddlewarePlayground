//
//  IOSUserViewModel.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import Shared

extension UserScreen {
    @MainActor class IOSUserViewModel: ObservableObject {
        private var getCurrentUserUseCase: User_domainGetCurrentUserUseCase
        private var updateUserUseCase: User_domainUpdateUserUseCase
        private var updatePasswordUseCase: User_domainUpdatePasswordUseCase
        private var deleteUserUseCase: User_domainDeleteUserUseCase
        private var logoutUseCase: User_domainLogoutUseCase
        
        private let viewModel: UserViewModel
        
        @Published
        var state: UserState = UserState(userData: nil, userName: "", phone: "", email: "", password: "", newPassword: "", repeatedPassword: "", isLoading: false)
        
        @Published
        var eventFlow: StateCommonFlow<UserViewModel.UiEvent>

        private var handler: StateDisposableHandle?
        
        init(
            getCurrentUserUseCase: User_domainGetCurrentUserUseCase,
            updateUserUseCase: User_domainUpdateUserUseCase,
            updatePasswordUseCase: User_domainUpdatePasswordUseCase,
            deleteUserUseCase: User_domainDeleteUserUseCase,
            logoutUseCase: User_domainLogoutUseCase
        ) {
            self.getCurrentUserUseCase = getCurrentUserUseCase
            self.updateUserUseCase = updateUserUseCase
            self.updatePasswordUseCase = updatePasswordUseCase
            self.deleteUserUseCase = deleteUserUseCase
            self.logoutUseCase = logoutUseCase
            
            self.viewModel = UserViewModel(
                coroutineScope: nil,
                getCurrentUserUseCase: getCurrentUserUseCase,
                updateUserUseCase: updateUserUseCase,
                updatePasswordUseCase: updatePasswordUseCase,
                deleteUserUseCase: deleteUserUseCase,
                logoutUseCase: logoutUseCase
            )
            self.eventFlow = viewModel.eventFlow
        }
        
        func onEvent(event: UserEvent) {
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
