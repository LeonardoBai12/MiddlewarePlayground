//
//  RouteDetailsScreen.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct RouteDetailsScreen: View {
    private var requestMappedRouteUseCase: Middleware_domainRequestMappedRouteUseCase
    private var saveRouteInHistoryUseCase: Middleware_domainSaveRouteInHistoryUseCase
    @ObservedObject private var viewModel: IOSRouteDetailsViewModel
    
    init(
        requestMappedRouteUseCase: Middleware_domainRequestMappedRouteUseCase,
        saveRouteInHistoryUseCase: Middleware_domainSaveRouteInHistoryUseCase
    ) {
        self.requestMappedRouteUseCase = requestMappedRouteUseCase
        self.saveRouteInHistoryUseCase = saveRouteInHistoryUseCase
        self.viewModel = IOSRouteDetailsViewModel(
            requestMappedRouteUseCase: requestMappedRouteUseCase,
            saveRouteInHistoryUseCase: saveRouteInHistoryUseCase
        )
    }
    
    var body: some View {
        Text("RouteDetails")
    }
}
