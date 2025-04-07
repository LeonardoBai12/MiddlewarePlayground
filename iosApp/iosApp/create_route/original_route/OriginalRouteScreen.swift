//
//  OriginalRouteScreen.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct OriginalRouteScreen: View {
    private var testOriginalRouteUseCase: Middleware_domainTestOriginalRouteUseCase
    @ObservedObject private var viewModel: IOSOriginalRouteViewModel
    
    init(
        testOriginalRouteUseCase: Middleware_domainTestOriginalRouteUseCase
    ) {
        self.testOriginalRouteUseCase = testOriginalRouteUseCase
        self.viewModel = IOSOriginalRouteViewModel(
            testOriginalRouteUseCase: testOriginalRouteUseCase
        )
    }
    
    var body: some View {
        Text("OriginalRoute")
    }
}
