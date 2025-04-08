//
//  FillRouteScreen.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct FillRouteFieldsScreen: View {
    private var args: CreateRouteArgs
    @ObservedObject private var viewModel: IOSFillRouteFieldsViewModel
    
    init(args: CreateRouteArgs) {
        self.args = args
        self.viewModel = IOSFillRouteFieldsViewModel()
    }
    
    var body: some View {
        Text("FillRouteFields")
    }
}
