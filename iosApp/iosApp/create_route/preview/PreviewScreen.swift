//
//  PreviewScreen.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct PreviewScreen: View {
    private var requestPreviewUseCase: Middleware_domainRequestPreviewUseCase
    @ObservedObject private var viewModel: IOSPreviewViewModel

    init(
        requestPreviewUseCase: Middleware_domainRequestPreviewUseCase
    ) {
        self.requestPreviewUseCase = requestPreviewUseCase
        self.viewModel = IOSPreviewViewModel(
            requestPreviewUseCase: requestPreviewUseCase
        )
    }
    
    var body: some View {
        Text("Preview")
    }
}
