//
//  IOSFillRouteFieldsViewModel.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import Shared

extension FillRouteFieldsScreen {
    @MainActor class IOSFillRouteFieldsViewModel: ObservableObject {
        private let viewModel: FillRouteFieldsViewModel
        
        @Published
        var state: FillRouteFieldsState = FillRouteFieldsState(newBodyFields: [:], oldBodyFields: [:])
        
        @Published
        var eventFlow: StateCommonFlow<FillRouteFieldsViewModel.UiEvent>

        private var handler: StateDisposableHandle?
        
        init() {
            self.viewModel = FillRouteFieldsViewModel(
                coroutineScope: nil
            )
            self.eventFlow = viewModel.eventFlow
        }
        
        func onEvent(event: FillRouteFieldsEvent) {
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
