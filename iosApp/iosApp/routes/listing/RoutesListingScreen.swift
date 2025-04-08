//
//  RoutesListingScreen.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct RoutesListingScreen: View {
    @Environment(\.navigate) private var navigate
    private var getAllRoutesUseCase: Middleware_domainGetAllRoutesUseCase
    @ObservedObject private var viewModel: IOSRoutesViewModel
    @State private var eventSubscription: StateDisposableHandle?
    
    @State private var searchFilter = ""
    @State private var groupingOption = 0
    @State private var showErrorAlert = false
    @State private var errorMessage = ""
    
    init(getAllRoutesUseCase: Middleware_domainGetAllRoutesUseCase) {
        self.getAllRoutesUseCase = getAllRoutesUseCase
        self.viewModel = IOSRoutesViewModel(getAllRoutesUseCase: getAllRoutesUseCase)
    }
    
    var body: some View {
        ZStack {
            Color.background.edgesIgnoringSafeArea(.all)
            contentView
        }
        .searchable(text: $searchFilter, placement: .navigationBarDrawer, prompt: "Search routes")
        .navigationTitle("Routes")
        .toolbar { toolbarContent }
        .onAppear { setupScreen() }
        .onDisappear { cleanup() }
        .alert("Error", isPresented: $showErrorAlert) {
            Button("OK", role: .cancel) { }
        } message: {
            Text(errorMessage)
        }
    }
    
    // MARK: - Subviews
    
    private var contentView: some View {
        VStack(spacing: 0) {
            groupingPicker
            routesContent
        }
    }
    
    private var groupingPicker: some View {
        Picker("Grouping Options", selection: $groupingOption) {
            Text("Group by APIs").tag(0)
            Text("Show all routes").tag(1)
        }
        .pickerStyle(SegmentedPickerStyle())
        .padding()
        .disabled(viewModel.state.isLoading)
    }
    
    @ViewBuilder
    private var routesContent: some View {
        if viewModel.state.isLoading {
            ProgressView()
                .padding(.top, 16)
            Spacer()
                .frame(maxHeight: .infinity)
        } else {
            List {
                if groupingOption == 0 {
                    groupedByApiContent
                } else {
                    allRoutesContent
                }
            }
            .listStyle(PlainListStyle())
            .background(Color.background)
        }
    }
    
    private var groupedByApiContent: some View {
        ForEach(filteredApis, id: \.self) { api in
            ApiSection(
                api: api,
                routes: viewModel.state.apis[api]?.map(MappedRoute.init) ?? []
            )
        }
    }
    
    private var allRoutesContent: some View {
        ForEach(filteredRoutes) { route in
            FullRouteCard(route: route)
        }
    }
    
    private var toolbarContent: some ToolbarContent {
        Group {
            ToolbarItem(placement: .navigationBarLeading) {
                userButton
            }
            ToolbarItem(placement: .navigationBarTrailing) {
                refreshButton
            }
            ToolbarItem(placement: .navigationBarTrailing) {
                addRouteButton
            }
        }
    }
    
    private var userButton: some View {
        Button(action: {navigate(.user)}) {
            Image(systemName: "person.fill")
                .foregroundColor(Color.primaryColor)
        }
        .disabled(viewModel.state.isLoading)
    }
    
    private var refreshButton: some View {
        Button(action: refresh) {
            if !viewModel.state.isLoading {
                Image(systemName: "arrow.clockwise")
                    .foregroundColor(Color.primaryColor)
            }
        }
        .disabled(viewModel.state.isLoading)
    }
    
    private var addRouteButton: some View {
        Button(action: {navigate(.originalRoute)}) {
            Image(systemName: "plus")
                .foregroundColor(Color.primaryColor)
        }
        .disabled(viewModel.state.isLoading)
    }
    
    // MARK: - Computed Properties
    
    private var filteredApis: [String] {
        if searchFilter.isEmpty {
            return viewModel.state.apis.keys.sorted()
        }
        
        return viewModel.state.apis.keys.filter { api in
            api.localizedCaseInsensitiveContains(searchFilter) ||
            viewModel.state.apis[api]?.contains { route in
                routeMatchesSearch(MappedRoute.init(from: route))
            } ?? false
        }.sorted()
    }
    
    private var filteredRoutes: [MappedRoute] {
        viewModel.state.routes
            .map(MappedRoute.init)
            .filter { route in
                searchFilter.isEmpty || routeMatchesSearch(route)
            }
            .sorted { $0.path < $1.path }
    }
    
    private func routeMatchesSearch(_ route: MappedRoute) -> Bool {
        route.originalBaseUrl.localizedCaseInsensitiveContains(searchFilter) ||
        route.originalPath.localizedCaseInsensitiveContains(searchFilter) ||
        route.path.localizedCaseInsensitiveContains(searchFilter)
    }
    
    // MARK: - Actions
    
    private func refresh() {
        viewModel.onEvent(event: RoutesEvent.GetRoutes())
    }
    
    private func setupScreen() {
        viewModel.startObserving()
        subscribeToEvents()
        if viewModel.state.routes.isEmpty {
            viewModel.onEvent(event: RoutesEvent.GetRoutes())
        }
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
    
    private func handleUiEvent(_ event: RoutesViewModel.UiEvent) {
        if let errorEvent = event as? RoutesViewModel.UiEventShowError {
            errorMessage = errorEvent.message
            showErrorAlert = true
        }
    }
}

// MARK: - Subcomponents

private struct ApiSection: View {
    let api: String
    let routes: [MappedRoute]
    @State private var isExpanded = false
    
    var body: some View {
        DisclosureGroup(isExpanded: $isExpanded) {
            ForEach(routes) { route in
                RouteCard(route: route)
            }
        } label: {
            DefaultCard(onClick: {
                isExpanded.toggle()
            }) {
                VStack(alignment: .leading) {
                    Text("Original Base URL")
                        .font(.caption)
                        .foregroundColor(Color.primaryColor)
                    Text(api.urlWithoutProtocolAndPath)
                        .font(.caption)
                        .foregroundColor(Color.onBackground)
                }
            }
        }
        .listRowBackground(Color.background)
    }
}

private struct RouteCard: View {
    @Environment(\.navigate) private var navigate
    let route: MappedRoute
    
    var body: some View {
        DefaultCard(onClick: {
            navigate(.routeDetails(route))
        }) {
            VStack(alignment: .leading, spacing: 8) {
                VStack(alignment: .leading, spacing: 2) {
                    Text("Original Path")
                        .font(.caption)
                        .foregroundColor(Color.primaryColor)
                    Text(route.originalPath)
                        .font(.caption)
                        .foregroundColor(Color.onBackground)
                }
                
                VStack(alignment: .leading, spacing: 2) {
                    Text("Mapped Path")
                        .font(.caption)
                        .foregroundColor(Color.primaryColor)
                    Text(route.path)
                        .font(.caption)
                        .foregroundColor(Color.onBackground)
                }
            }
        }
        .listRowBackground(Color.background)
    }
}

private struct FullRouteCard: View {
    @Environment(\.navigate) private var navigate
    let route: MappedRoute
    
    var body: some View {
        DefaultCard(onClick: {
            navigate(.routeDetails(route))
        }) {
            VStack(alignment: .leading, spacing: 8) {
                VStack(alignment: .leading, spacing: 2) {
                    Text("Original Base URL")
                        .font(.caption)
                        .foregroundColor(Color.primaryColor)
                    Text(route.originalBaseUrl.urlWithoutProtocolAndPath)
                        .font(.caption)
                        .foregroundColor(Color.onBackground)
                }
                
                VStack(alignment: .leading, spacing: 2) {
                    Text("Original Path")
                        .font(.caption)
                        .foregroundColor(Color.primaryColor)
                    Text(route.originalPath)
                        .font(.caption)
                        .foregroundColor(Color.onBackground)
                }
                
                VStack(alignment: .leading, spacing: 2) {
                    Text("Mapped Path")
                        .font(.caption)
                        .foregroundColor(Color.primaryColor)
                    Text(route.path)
                        .font(.caption)
                        .foregroundColor(Color.onBackground)
                }
            }
        }
        .listRowBackground(Color.background)
    }
}

// MARK: - Extensions

private extension String {
    var urlWithoutProtocolAndPath: String {
        self.components(separatedBy: "://").last?.components(separatedBy: "/").first ?? self
    }
}
