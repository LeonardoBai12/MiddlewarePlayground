//
//  FillPreConfigsScreen.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct FillPreConfigsScreen: View {
    @ObservedObject private var viewModel: IOSFillPreConfigsViewModel

    init() {
        self.viewModel = IOSFillPreConfigsViewModel()
    }
    
    var body: some View {
        Text("FillPreConfigs")
    }
}
