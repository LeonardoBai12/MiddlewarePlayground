//
//  IOSSplashViewModel.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import Shared

extension SplashScreen {
    @MainActor class IOSSplashViewModel: ObservableObject {
        private var getCurrentUserOnInitUseCase: Splash_domainGetCurrentUserOnInitUseCase
        
        private let viewModel: SplashViewModel
        
        @Published
        var eventFlow: StateCommonFlow<SplashViewModel.UiEvent>

        private var handler: StateDisposableHandle?
        
        init(
            getCurrentUserOnInitUseCase: Splash_domainGetCurrentUserOnInitUseCase
        ) {
            self.getCurrentUserOnInitUseCase = getCurrentUserOnInitUseCase
            self.viewModel = SplashViewModel(
                coroutineScope: nil,
                getCurrentUserOnInitUseCase: getCurrentUserOnInitUseCase
            )
            self.eventFlow = viewModel.eventFlow
        }
        
        func onEvent(event: SplashEvent) {
            self.viewModel.onEvent(event: event)
        }
        
        func dispose() {
            handler?.dispose()
        }
    }
}
