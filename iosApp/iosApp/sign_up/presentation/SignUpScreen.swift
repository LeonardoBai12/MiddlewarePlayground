//
//  SignUpScreen.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct SignUpScreen: View {
    private var signUpUseCase: Sign_up_domainSignUpUseCase
    private var loginUseCase: Sign_up_domainLoginUseCase
    
    @ObservedObject private var viewModel: IOSSignUpViewModel
    
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
        Text("SignUp")
    }
}
