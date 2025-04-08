//
//  UserScreen.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct UserScreen: View {
    private var getCurrentUserUseCase: User_domainGetCurrentUserUseCase
    private var updateUserUseCase: User_domainUpdateUserUseCase
    private var updatePasswordUseCase: User_domainUpdatePasswordUseCase
    private var deleteUserUseCase: User_domainDeleteUserUseCase
    private var logoutUseCase: User_domainLogoutUseCase
    @ObservedObject private var viewModel: IOSUserViewModel
    
    @State private var showErrorAlert = false
    @State private var errorMessage = ""
    @State private var eventSubscription: StateDisposableHandle?
    @Environment(\.replace) private var navigate
    
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
        self.viewModel = IOSUserViewModel(
            getCurrentUserUseCase: getCurrentUserUseCase,
            updateUserUseCase: updateUserUseCase,
            updatePasswordUseCase: updatePasswordUseCase,
            deleteUserUseCase: deleteUserUseCase,
            logoutUseCase: logoutUseCase
        )
    }
    
    var body: some View {
        ScrollView {
            VStack(spacing: 24) {
                // User Avatar
                ZStack {
                    Circle()
                        .fill(Color.primaryColor)
                        .frame(width: 100, height: 100)
                    
                    Text(viewModel.state.userData?.userName.first?.uppercased() ?? "?")
                        .font(.system(size: 48, weight: .bold))
                        .foregroundColor(Color.onPrimary)
                }
                .padding(.top, 32)
                
                // User Info
                VStack(spacing: 8) {
                    Text(viewModel.state.userData?.userName ?? "No name")
                        .font(.title2)
                        .foregroundColor(Color.onBackground)
                    
                    if let email = viewModel.state.userData?.email {
                        Text(email)
                            .font(.subheadline)
                            .foregroundColor(Color.onBackground.opacity(0.7))
                    }
                    
                    if let phone = viewModel.state.userData?.phone {
                        Text(phone)
                            .font(.subheadline)
                            .foregroundColor(Color.onBackground.opacity(0.7))
                    }
                }
                
                // Logout Button
                DefaultTextButton(
                    text: "Logout",
                    onClick: {
                        viewModel.onEvent(event: UserEvent.Logout())
                    },
                    containerColor: Color.surface,
                    contentColor: Color.onSurface
                )
                .padding(.horizontal)
                .padding(.bottom, 32)
            }
            .frame(maxWidth: .infinity)
        }
        .background(Color.background)
        .navigationTitle("User Profile")
        .onAppear {
            setupScreen()
            viewModel.onEvent(event: UserEvent.GetCurrentUser())
        }
        .onDisappear {
            cleanup()
        }
        .alert("Error", isPresented: $showErrorAlert) {
            Button("OK", role: .cancel) { }
        } message: {
            Text(errorMessage)
        }
    }
    
    private func setupScreen() {
        viewModel.startObserving()
        subscribeToEvents()
    }
    
    private func cleanup() {
        viewModel.dispose()
        eventSubscription?.dispose()
    }
    
    private func subscribeToEvents() {
        eventSubscription = viewModel.eventFlow.subscribe(
            onCollect: { event in
                DispatchQueue.main.async {
                    guard let event = event else { return }
                    handleUiEvent(event)
                }
            }
        )
    }
    
    private func handleUiEvent(_ event: UserViewModel.UiEvent) {
        switch event {
        case is UserViewModel.UiEventShowLoggedOutMessage:
            // Navigate to splash screen when logged out
            navigate(.splash)
            
        case let showError as UserViewModel.UiEventShowError:
            errorMessage = showError.message
            showErrorAlert = true
            
        default:
            break
        }
    }
}
