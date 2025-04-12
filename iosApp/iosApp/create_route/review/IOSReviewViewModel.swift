//
//  IOSReviewViewModel.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import Shared

extension ReviewScreen {
    @MainActor class IOSReviewViewModel: ObservableObject {
        private var createNewRouteUseCase: Middleware_domainCreateNewRouteUseCase
        
        private let viewModel: ReviewViewModel
        
        @Published
        var state: ReviewState = ReviewState(isLoading: false)
        
        @Published
        var eventFlow: StateCommonFlow<ReviewViewModel.UiEvent>

        private var handler: StateDisposableHandle?
        
        init(
            createNewRouteUseCase: Middleware_domainCreateNewRouteUseCase
        ) {
            self.createNewRouteUseCase = createNewRouteUseCase
            self.viewModel = ReviewViewModel(
                coroutineScope: nil,
                createNewRouteUseCase: createNewRouteUseCase
            )
            self.eventFlow = viewModel.eventFlow
        }
        
        func onEvent(event: ReviewEvent) {
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
