//
//  IOSOriginalRouteViewModel.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import Shared

extension OriginalRouteScreen {
    @MainActor class IOSOriginalRouteViewModel: ObservableObject {
        private var testOriginalRouteUseCase: Middleware_domainTestOriginalRouteUseCase
        
        private let viewModel: OriginalRouteViewModel
        
        @Published
        var state: OriginalRouteState = OriginalRouteState(originalQueries: [:], originalHeaders: [:], isLoading: false)
        
        @Published
        var eventFlow: StateCommonFlow<OriginalRouteViewModel.UiEvent>

        private var handler: StateDisposableHandle?
        
        init(
            testOriginalRouteUseCase: Middleware_domainTestOriginalRouteUseCase
        ) {
            self.testOriginalRouteUseCase = testOriginalRouteUseCase
            self.viewModel = OriginalRouteViewModel(
                coroutineScope: nil,
                testOriginalRouteUseCase: testOriginalRouteUseCase
            )
            self.eventFlow = viewModel.eventFlow
        }
        
        func onEvent(event: OriginalRouteEvent) {
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
