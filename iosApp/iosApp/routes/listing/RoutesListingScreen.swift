//
//  RoutesListingScreen.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct RoutesListingScreen: View {
    private var getAllRoutesUseCase: Middleware_domainGetAllRoutesUseCase
    @ObservedObject private var viewModel: IOSRoutesViewModel
    
    init(
        getAllRoutesUseCase: Middleware_domainGetAllRoutesUseCase
    ) {
        self.getAllRoutesUseCase = getAllRoutesUseCase
        self.viewModel = IOSRoutesViewModel(
            getAllRoutesUseCase: getAllRoutesUseCase
        )
    }
    
    var body: some View {
        Text("Routes")
    }
}
