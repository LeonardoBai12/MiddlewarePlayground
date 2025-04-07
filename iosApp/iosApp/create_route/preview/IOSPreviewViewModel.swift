//
//  IOSPreviewViewModel.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import Shared

extension PreviewScreen {
    @MainActor class IOSPreviewViewModel: ObservableObject {
        private var requestPreviewUseCase: Middleware_domainRequestPreviewUseCase
        
        private let viewModel: PreviewViewModel
        
        @Published
        var state: PreviewState = PreviewState(isLoading: false)
        
        @Published
        var eventFlow: StateCommonFlow<PreviewViewModel.UiEvent>

        private var handler: StateDisposableHandle?
        
        init(
            requestPreviewUseCase: Middleware_domainRequestPreviewUseCase
        ) {
            self.requestPreviewUseCase = requestPreviewUseCase
            self.viewModel = PreviewViewModel(
                coroutineScope: nil,
                requestPreviewUseCase: requestPreviewUseCase
            )
            self.eventFlow = viewModel.eventFlow
        }
        
        func onEvent(event: PreviewEvent) {
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
