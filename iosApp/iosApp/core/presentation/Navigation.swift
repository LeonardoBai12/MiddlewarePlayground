//
//  NavigationKey.swift
//  LBTasks-iOS
//
//  Created by Leonardo Bai on 06/04/25.
//

import SwiftUICore

enum AppScreen: String, CaseIterable, Identifiable {
    case splash, signUp, routeListing, routeDetails, fillRouteFields
    case fillPreConfigs, preview, originalRoute, review, user
    
    var id: String { self.rawValue }
}

struct NavigationKey: EnvironmentKey {
    static let defaultValue: (AppScreen) -> Void = { _ in }
}

extension EnvironmentValues {
    var navigate: (AppScreen) -> Void {
        get { self[NavigationKey.self] }
        set { self[NavigationKey.self] = newValue }
    }
}
