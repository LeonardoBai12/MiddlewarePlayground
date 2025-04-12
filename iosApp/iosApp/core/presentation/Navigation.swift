//
//  NavigationKey.swift
//  LBTasks-iOS
//
//  Created by Leonardo Bai on 06/04/25.
//

import SwiftUICore

enum AppScreen: Hashable, Identifiable {
    case splash
    case signUp
    case login
    case signIn
    case routeListing
    case routeDetails(MappedRoute)
    case fillRouteFields(CreateRouteArgs)
    case fillPreConfigs(CreateRouteArgs)
    case preview(CreateRouteArgs)
    case originalRoute
    case review(CreateRouteArgs)
    case user
    
    var id: String {
        switch self {
        case .splash: return "splash"
        case .signUp: return "signUp"
        case .login: return "login"
        case .signIn: return "signIn"
        case .routeListing: return "routeListing"
        case .routeDetails(let route): return "routeDetails_\(route.id)"
        case .fillRouteFields(let args): return "fillRouteFields_\(args.id)"
        case .fillPreConfigs(let args): return "fillPreConfigs_\(args.id)"
        case .preview(let args): return "preview_\(args.id)"
        case .originalRoute: return "originalRoute"
        case .review(let args): return "review_\(args.id)"
        case .user: return "user"
        }
    }
}

extension AppScreen: Equatable {
    static func == (lhs: AppScreen, rhs: AppScreen) -> Bool {
        switch (lhs, rhs) {
        case (.splash, .splash): return true
        case (.signUp, .signUp): return true
        case (.login, .login): return true
        case (.signIn, .signIn): return true
        case (.routeListing, .routeListing): return true
        case (.routeDetails(let lhsRoute), .routeDetails(let rhsRoute)):
            return lhsRoute == rhsRoute
        case (.fillRouteFields, .fillRouteFields): return true
        case (.fillPreConfigs, .fillPreConfigs): return true
        case (.preview, .preview): return true
        case (.originalRoute, .originalRoute): return true
        case (.review, .review): return true
        case (.user, .user): return true
        default: return false
        }
    }
}

struct NavigationKey: EnvironmentKey {
    static let defaultValue: (AppScreen) -> Void = { _ in }
}

struct ReplaceNavigationKey: EnvironmentKey {
    static let defaultValue: (AppScreen) -> Void = { _ in }
}

extension EnvironmentValues {
    var navigate: (AppScreen) -> Void {
        get { self[NavigationKey.self] }
        set { self[NavigationKey.self] = newValue }
    }
    
    var replace: (AppScreen) -> Void {
        get { self[ReplaceNavigationKey.self] }
        set { self[ReplaceNavigationKey.self] = newValue }
    }
}
