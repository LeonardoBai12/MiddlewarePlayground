//
//  FillPreConfigsScreen.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct FillPreConfigsScreen: View {
    private var args: CreateRouteArgs
    @ObservedObject private var viewModel: IOSFillPreConfigsViewModel

    init(args: CreateRouteArgs) {
        self.args = args
        self.viewModel = IOSFillPreConfigsViewModel()
    }
    
    var body: some View {
        Text("FillPreConfigs")
    }
}
