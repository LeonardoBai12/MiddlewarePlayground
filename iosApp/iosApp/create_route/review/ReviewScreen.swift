//
//  ReviewScreen.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct ReviewScreen: View {
    private var args: CreateRouteArgs
    private var createNewRouteUseCase: Middleware_domainCreateNewRouteUseCase
    @ObservedObject private var viewModel: IOSReviewViewModel
    
    init(
        args: CreateRouteArgs,
        createNewRouteUseCase: Middleware_domainCreateNewRouteUseCase
    ) {
        self.args = args
        self.createNewRouteUseCase = createNewRouteUseCase
        self.viewModel = IOSReviewViewModel(
            createNewRouteUseCase: createNewRouteUseCase
        )
    }
    
    var body: some View {
        Text("Review")
    }
}
