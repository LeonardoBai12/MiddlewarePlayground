//
//  IOSRouteDetailsViewModel.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import Shared

extension RouteDetailsScreen {
    @MainActor class IOSRouteDetailsViewModel: ObservableObject {
        private var requestMappedRouteUseCase: Middleware_domainRequestMappedRouteUseCase
        private var saveRouteInHistoryUseCase: Middleware_domainSaveRouteInHistoryUseCase
        
        private let viewModel: RouteDetailsViewModel
        
        @Published
        var state: RouteDetailsState = RouteDetailsState(mappedRoute: nil, isLoading: false)
        
        @Published
        var eventFlow: StateCommonFlow<RouteDetailsViewModel.UiEvent>

        private var handler: StateDisposableHandle?
        
        init(
            requestMappedRouteUseCase: Middleware_domainRequestMappedRouteUseCase,
            saveRouteInHistoryUseCase: Middleware_domainSaveRouteInHistoryUseCase
        ) {
            self.requestMappedRouteUseCase = requestMappedRouteUseCase
            self.saveRouteInHistoryUseCase = saveRouteInHistoryUseCase
            self.viewModel = RouteDetailsViewModel(
                coroutineScope: nil,
                requestMappedRouteUseCase: requestMappedRouteUseCase,
                saveRouteInHistoryUseCase: saveRouteInHistoryUseCase
            )
            self.eventFlow = viewModel.eventFlow
        }
        
        func onEvent(event: RouteDetailsEvent) {
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
