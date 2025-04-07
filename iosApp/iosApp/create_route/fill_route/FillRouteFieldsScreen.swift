//
//  FillRouteScreen.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct FillRouteFieldsScreen: View {
    @ObservedObject private var viewModel: IOSFillRouteFieldsViewModel
    
    init() {
        self.viewModel = IOSFillRouteFieldsViewModel()
    }
    
    var body: some View {
        Text("FillRouteFields")
    }
}
