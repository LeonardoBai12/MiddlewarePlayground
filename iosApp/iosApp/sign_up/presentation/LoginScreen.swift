//  SignUpScreen.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct LoginScreen: View {
    private let signUpUseCase: Sign_up_domainSignUpUseCase
    private let loginUseCase: Sign_up_domainLoginUseCase
    @ObservedObject private var viewModel: IOSSignUpViewModel
    @State private var eventSubscription: StateDisposableHandle?

    @State private var showAuthSheet = false
    @State private var isLoginMode = true
    @State private var showErrorAlert = false
    @State private var errorMessage = ""
    
    init(
        signUpUseCase: Sign_up_domainSignUpUseCase,
        loginUseCase: Sign_up_domainLoginUseCase
    ) {
        self.signUpUseCase = signUpUseCase
        self.loginUseCase = loginUseCase
        self.viewModel = IOSSignUpViewModel(
            signUpUseCase: signUpUseCase,
            loginUseCase: loginUseCase
        )
    }
    
    var body: some View {
        ZStack {
            Text("Login Screen")
                .font(.largeTitle)
                .foregroundColor(.white)
                .padding()
        }
        .onAppear {
            subscribeToEvents()
        }
        .onDisappear {
            viewModel.dispose()
            eventSubscription?.dispose()
        }
        .alert("Error", isPresented: $showErrorAlert) {
            Button("OK", role: .cancel) {}
        } message: {
            Text(errorMessage)
        }
    }
    
    private func subscribeToEvents() {
        eventSubscription = viewModel.eventFlow.subscribe(
            onCollect: { event in
                guard let event = event else { return }
                DispatchQueue.main.async {
                    handleUiEvent(event)
                }
            }
        )
    }
    
    private func handleUiEvent(_ event: SignUpViewModel.UiEvent) {
        switch event {
        case let event as SignUpViewModel.UiEventShowError:
            errorMessage = event.message
            showErrorAlert = true
            
        case is SignUpViewModel.UiEventShowLoginSuccess,
             is SignUpViewModel.UiEventShowSignUpSuccess:
            // Navigation handled automatically by parent
            break
            
        default:
            break
        }
    }
}
