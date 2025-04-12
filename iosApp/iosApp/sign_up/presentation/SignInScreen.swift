//  SignUpScreen.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct SignInScreen: View {
    @Environment(\.replace) private var navigate
    @ObservedObject private var viewModel: IOSSignUpViewModel
    @State private var eventSubscription: StateDisposableHandle?
    
    @State private var name = ""
    @State private var email = ""
    @State private var phone = ""
    @State private var password = ""
    @State private var repeatedPassword = ""
    @State private var showErrorAlert = false
    @State private var errorMessage = ""
    
    init(
        signUpUseCase: Sign_up_domainSignUpUseCase,
        loginUseCase: Sign_up_domainLoginUseCase
    ) {
        self.viewModel = IOSSignUpViewModel(
            signUpUseCase: signUpUseCase,
            loginUseCase: loginUseCase
        )
    }
    
    var body: some View {
        ScrollView {
            VStack(spacing: 24) {
                // Header
                VStack(spacing: 12) {
                    MiddlewareLogoIcon(size: CGSize(width: 80, height: 80))
                    Text("Create Account")
                        .font(.title2)
                        .foregroundColor(Color.onBackground)
                }
                .padding(.top, 32)
                
                // Form
                VStack(spacing: 16) {
                    DefaultTextField(
                        text: $name,
                        label: "Full Name",
                        icon: Image(systemName: "person"),
                        isEnabled: !viewModel.state.isLoading
                    )
                    
                    DefaultTextField(
                        text: $email,
                        label: "Email",
                        icon: Image(systemName: "envelope"),
                        isEnabled: !viewModel.state.isLoading,
                        keyboardType: .emailAddress
                    )
                    
                    DefaultTextField(
                        text: $phone,
                        label: "Phone",
                        icon: Image(systemName: "phone"),
                        isEnabled: !viewModel.state.isLoading,
                        keyboardType: .phonePad
                    )
                    
                    DefaultTextField(
                        text: $password,
                        label: "Password",
                        icon: Image(systemName: "lock"),
                        isPassword: true,
                        isEnabled: !viewModel.state.isLoading
                    )
                    
                    DefaultTextField(
                        text: $repeatedPassword,
                        label: "Repeat Password",
                        icon: Image(systemName: "lock"),
                        isPassword: true,
                        isEnabled: !viewModel.state.isLoading
                    )
                    Spacer()
                    DefaultTextButton(
                        text: viewModel.state.isLoading ? "Creating Account..." : "Create Account",
                        onClick: handleSignUp,
                        enabled: !viewModel.state.isLoading && isFormValid
                    )
                }
                .padding(.horizontal, 24)
                
                Spacer()
            }
            .background(Color.background)
            .navigationTitle("Sign Up")
            .navigationBarTitleDisplayMode(.inline)
        }
        .background(Color.background)
        .onAppear {
            viewModel.startObserving()
            subscribeToEvents()
        }
        .onDisappear {
            viewModel.dispose()
            eventSubscription?.dispose()
        }
        .alert("Error", isPresented: $showErrorAlert) {
            Button("OK", role: .cancel) { }
        } message: {
            Text(errorMessage)
        }
    }
    
    private var isFormValid: Bool {
        !name.isEmpty &&
        !email.isEmpty &&
        !phone.isEmpty &&
        !password.isEmpty &&
        password == repeatedPassword
    }
    
    private func handleSignUp() {
        viewModel.onEvent(event: SignUpEvent.RequestSignUp(
            userName: name,
            phone: phone,
            email: email,
            profilePictureUrl: nil,
            password: password,
            repeatedPassword: repeatedPassword
        ))
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
    
    private func handleUiEvent(_ event: SignUpViewModel.UiEvent) {
        switch event {
        case let event as SignUpViewModel.UiEventShowError:
            errorMessage = event.message
            showErrorAlert = true
            
        case is SignUpViewModel.UiEventShowSignUpSuccess:
            navigate(.routeListing)
            
        default: break
        }
    }
}
