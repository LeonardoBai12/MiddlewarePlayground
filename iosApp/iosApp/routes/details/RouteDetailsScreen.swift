//
//  RouteDetailsScreen.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct RouteDetailsScreen: View {
    private var route: MappedRoute
    private var requestMappedRouteUseCase: Middleware_domainRequestMappedRouteUseCase
    private var saveRouteInHistoryUseCase: Middleware_domainSaveRouteInHistoryUseCase
    @ObservedObject private var viewModel: IOSRouteDetailsViewModel
    
    @State private var result = ""
    @State private var showErrorAlert = false
    @State private var errorMessage = ""
    @State private var eventSubscription: StateDisposableHandle?
    
    init(
        route: MappedRoute,
        requestMappedRouteUseCase: Middleware_domainRequestMappedRouteUseCase,
        saveRouteInHistoryUseCase: Middleware_domainSaveRouteInHistoryUseCase
    ) {
        self.route = route
        self.requestMappedRouteUseCase = requestMappedRouteUseCase
        self.saveRouteInHistoryUseCase = saveRouteInHistoryUseCase
        self.viewModel = IOSRouteDetailsViewModel(
            requestMappedRouteUseCase: requestMappedRouteUseCase,
            saveRouteInHistoryUseCase: saveRouteInHistoryUseCase
        )
    }
    
    var body: some View {
        ScrollView {
            VStack(spacing: 16) {
                VStack(alignment: .leading, spacing: 4) {
                    Text("Original Base URL")
                        .font(.system(size: 16))
                        .foregroundColor(Color.primaryColor)
                    Text(route.originalBaseUrl.urlWithoutProtocolAndPath)
                        .font(.system(size: 16))
                        .foregroundColor(Color.onBackground)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal)
                
                // Original Path
                VStack(alignment: .leading, spacing: 4) {
                    Text("Original Path")
                        .font(.system(size: 16))
                        .foregroundColor(Color.primaryColor)
                    Text(route.originalPath.urlWithoutProtocol)
                        .font(.system(size: 16))
                        .foregroundColor(Color.onBackground)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal)
                
                // Mapped Path
                VStack(alignment: .leading, spacing: 4) {
                    HStack {
                        Text("Mapped Path")
                            .font(.system(size: 16))
                            .foregroundColor(Color.primaryColor)
                        
                        Spacer()
                        
                        Button(action: copyMappedPath) {
                            Image(systemName: "doc.on.doc")
                                .foregroundColor(Color.primaryColor)
                        }
                    }
                    
                    Text(route.path.urlWithoutProtocol)
                        .font(.system(size: 16))
                        .foregroundColor(Color.onBackground)
                        .lineLimit(1)
                        .truncationMode(.tail)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal)
                
                // Method Boxes
                HStack(spacing: 24) {
                    MethodBox(method: route.originalMethod, text: "Original Method")
                    MethodBox(method: route.method, text: "Mapped Method")
                    Spacer()
                }
                .padding(.horizontal)
                
                // Route Details
                RouteDetails(
                    originalQueries: route.originalQueries,
                    originalHeaders: route.originalHeaders,
                    originalBody: route.originalBody,
                    preConfiguredQueries: route.preConfiguredQueries,
                    preConfiguredHeaders: route.preConfiguredHeaders,
                    preConfiguredBody: route.preConfiguredBody
                )
                
                // Test Column
                TestColumn(
                    isLoading: viewModel.state.isLoading,
                    result: result,
                    onClick: testRoute
                )
                .padding(.horizontal)
            }
            .padding(.vertical)
        }
        .background(Color.background)
        .navigationTitle("Route Details")
        .onAppear {
            setupScreen()
            viewModel.onEvent(event: RouteDetailsEvent.SaveRouteInHistory(mappedRoute: route.toModel()))
        }
        .onDisappear {
            cleanup()
        }
        .alert("Error", isPresented: $showErrorAlert) {
            Button("OK", role: .cancel) { }
        } message: {
            Text(errorMessage)
        }
    }
    
    private func testRoute() {
        result = ""
        viewModel.onEvent(event: RouteDetailsEvent.TestRoute())
    }
    
    private func copyMappedPath() {
        UIPasteboard.general.string = route.path
        // You might want to show a toast here
    }
    
    private func setupScreen() {
        viewModel.startObserving()
        subscribeToEvents()
    }
    
    private func cleanup() {
        viewModel.dispose()
        eventSubscription?.dispose()
    }
    
    private func subscribeToEvents() {
        eventSubscription = viewModel.eventFlow.subscribe(
            onCollect: { event in
                DispatchQueue.main.async {
                    guard let event = event else { return }
                    handleUiEvent(event)
                }
            }
        )
    }
    
    private func handleUiEvent(_ event: RouteDetailsViewModel.UiEvent) {
        switch event {
        case let showError as RouteDetailsViewModel.UiEventShowError:
            errorMessage = showError.message
            showErrorAlert = true
        case let showResult as RouteDetailsViewModel.UiEventShowResult:
            result = showResult.result
        default:
            break
        }
    }
}

// Helper extension for URL string manipulation
private extension String {
    var urlWithoutProtocolAndPath: String {
        self.replacingOccurrences(of: "https://", with: "")
            .replacingOccurrences(of: "http://", with: "")
            .components(separatedBy: "/").first ?? self
    }
    
    var urlWithoutProtocol: String {
        self.replacingOccurrences(of: "https://", with: "")
            .replacingOccurrences(of: "http://", with: "")
    }
}
