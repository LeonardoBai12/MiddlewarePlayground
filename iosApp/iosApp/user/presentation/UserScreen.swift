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
        Text("User")
    }
}
