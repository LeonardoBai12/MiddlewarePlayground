//
//  SplashScreen.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct SplashScreen: View {
    private var getCurrentUserOnInitUseCase: Splash_domainGetCurrentUserOnInitUseCase
    @ObservedObject private var viewModel: IOSSplashViewModel
    
    init(
        getCurrentUserOnInitUseCase: Splash_domainGetCurrentUserOnInitUseCase
    ) {
        self.getCurrentUserOnInitUseCase = getCurrentUserOnInitUseCase
        self.viewModel = IOSSplashViewModel(
            getCurrentUserOnInitUseCase: getCurrentUserOnInitUseCase
        )
    }
    
    var body: some View {
        Text("Splash")
            .onAppear {
                DispatchQueue.main.asyncAfter(deadline: .now() + 1.5) {
                    viewModel.onEvent(event: SplashEvent.GetCurrentUser())
                }
            }
            .onDisappear {
                viewModel.dispose()
            }
    }
}
