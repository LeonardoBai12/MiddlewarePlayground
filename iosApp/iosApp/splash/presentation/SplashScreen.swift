//
//  SplashScreen.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct SplashScreen: View {
    @Environment(\.navigate) private var navigate
    @StateObject private var viewModel: IOSSplashViewModel
    @State private var eventSubscription: StateDisposableHandle?
    
    init(getCurrentUserOnInitUseCase: Splash_domainGetCurrentUserOnInitUseCase) {
        _viewModel = StateObject(wrappedValue: IOSSplashViewModel(
            getCurrentUserOnInitUseCase: getCurrentUserOnInitUseCase
        ))
    }
    
    var body: some View {
        ZStack {
            Color.background.edgesIgnoringSafeArea(.all)
            
            Image("ProjectMiddlewareLogo_NoBG")
                .resizable()
                .scaledToFit()
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .padding(.horizontal, 40)
        }
        .onAppear {
            DispatchQueue.main.asyncAfter(deadline: .now() + 1.5) {
                viewModel.onEvent(event: SplashEvent.GetCurrentUser())
            }
            subscribeToEvents()
        }
        .onDisappear {
            viewModel.dispose()
            eventSubscription?.dispose()
        }
    }
    
    private func subscribeToEvents() {
        eventSubscription = viewModel.eventFlow.subscribe(
            onCollect: { event in
                guard let event = event else { return }
                DispatchQueue.main.async {
                    handleUiEvent(event)
                }
            }
        )
    }
    
    private func handleUiEvent(_ event: SplashViewModel.UiEvent) {
        switch event {
        case _ as SplashViewModel.UiEventNavigateToSignUp:
            navigate(.signUp)
            
        case _ as SplashViewModel.UiEventNavigateToHome:
            navigate(.routeListing)
            
        default:
            break
        }
    }
}
