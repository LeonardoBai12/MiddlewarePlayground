//
//  IOSRoutesViewModel.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import Shared

extension RoutesListingScreen {
    @MainActor class IOSRoutesViewModel: ObservableObject {
        private var getAllRoutesUseCase: Middleware_domainGetAllRoutesUseCase
        
        private let viewModel: RoutesViewModel
        
        @Published
        var state: RoutesState = RoutesState(routes: [], apis: [:], isLoading: false)
        
        @Published
        var eventFlow: StateCommonFlow<RoutesViewModel.UiEvent>

        private var handler: StateDisposableHandle?
        
        init(
            getAllRoutesUseCase: Middleware_domainGetAllRoutesUseCase
        ) {
            self.getAllRoutesUseCase = getAllRoutesUseCase
            self.viewModel = RoutesViewModel(
                coroutineScope: nil,
                getAllRoutesUseCase: getAllRoutesUseCase
            )
            self.eventFlow = viewModel.eventFlow
        }
        
        func onEvent(event: RoutesEvent) {
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
