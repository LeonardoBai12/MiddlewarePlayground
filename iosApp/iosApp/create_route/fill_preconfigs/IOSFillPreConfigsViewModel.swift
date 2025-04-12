//
//  IOSFillPreConfigsViewModel.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import Shared

extension FillPreConfigsScreen {
    @MainActor class IOSFillPreConfigsViewModel: ObservableObject {
        private let viewModel: FillPreConfigsViewModel
        
        @Published
        var state: FillPreConfigsState = FillPreConfigsState(preConfiguredQueries: [:], preConfiguredHeaders: [:], ignoreEmptyValues: false)
        
        @Published
        var eventFlow: StateCommonFlow<FillPreConfigsViewModel.UiEvent>

        private var handler: StateDisposableHandle?
        
        init() {
            self.viewModel = FillPreConfigsViewModel(
                coroutineScope: nil
            )
            self.eventFlow = viewModel.eventFlow
        }
        
        func onEvent(event: FillPreConfigsEvent) {
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
