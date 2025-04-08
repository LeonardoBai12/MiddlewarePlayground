//
//  OriginalRouteScreen.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct OriginalRouteScreen: View {
    private var testOriginalRouteUseCase: Middleware_domainTestOriginalRouteUseCase
    @ObservedObject private var viewModel: IOSOriginalRouteViewModel
    
    @State private var result = ""
    @State private var code = 0
    @State private var originalBaseUrl = "https://"
    @State private var originalPath = ""
    @State private var originalMethodExpanded = false
    @State private var originalMethod = Common_sharedMiddlewareHttpMethods.get
    @State private var originalBody = ""
    
    @State private var showErrorAlert = false
    @State private var errorMessage = ""
    @State private var eventSubscription: StateDisposableHandle?
    @Environment(\.navigate) private var navigate
    
    init(
        testOriginalRouteUseCase: Middleware_domainTestOriginalRouteUseCase
    ) {
        self.testOriginalRouteUseCase = testOriginalRouteUseCase
        self.viewModel = IOSOriginalRouteViewModel(
            testOriginalRouteUseCase: testOriginalRouteUseCase
        )
    }
    
    var body: some View {
        ScrollView {
            VStack(spacing: 16) {
                methodSelectionSection
                inputFieldsSection
                headersSection
                queriesSection
                testSection
            }
            .padding(.vertical)
        }
        .onAppear { setupScreen() }
        .onDisappear { cleanup() }
        .background(Color.background)
        .toolbar {
            ToolbarItem(placement: .navigationBarTrailing) {
                Button(
                    action: {
                        viewModel.onEvent(event: OriginalRouteEvent.MoveForward(
                            result: result,
                            code: Int32(code),
                            originalBaseUrl: originalBaseUrl,
                            originalPath: originalPath,
                            originalMethod: originalMethod,
                            originalBody: originalBody
                        ))
                    }
                ) {
                    HStack {
                        Text("Next")
                            .foregroundColor(Color.primaryColor)
                        Image(systemName: "chevron.right")
                            .foregroundColor(Color.primaryColor)
                    }
                }.disabled(viewModel.state.isLoading || result.isEmpty)
            }
        }
        .navigationTitle("Step 1/5: Original Route")
    }
    
    // MARK: - Subviews
    
    private var methodSelectionSection: some View {
        HStack {
            MethodBox(
                method: originalMethod,
                text: "Original Method"
            )
            .onTapGesture {
                originalMethodExpanded = true
            }
            .confirmationDialog(
                "Select Method",
                isPresented: $originalMethodExpanded,
                titleVisibility: .visible
            ) {
                ForEach(Common_sharedMiddlewareHttpMethods.entries, id: \.self) { method in
                    Button(method.name) {
                        originalMethod = method
                    }
                }
            }
            Spacer()
        }
        .padding(.horizontal)
    }
    
    private var inputFieldsSection: some View {
        VStack(spacing: 8) {
            DefaultTextField(
                text: $originalBaseUrl,
                label: "Original Base URL",
                isEnabled: !viewModel.state.isLoading
            )
            
            DefaultTextField(
                text: $originalPath,
                label: "Original Path",
                isEnabled: !viewModel.state.isLoading
            )
            
            DefaultTextField(
                text: $originalBody,
                label: "Original Body",
                isEnabled: !viewModel.state.isLoading,
                isSingleLined: false
            )
        }
        .padding(.horizontal)
    }
    
    private var headersSection: some View {
        Group {
            Text("Original Headers")
                .font(.headline)
                .foregroundColor(Color.primaryColor)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal)
            
            // Existing headers
            ForEach(Array(viewModel.state.originalHeaders.keys.sorted()), id: \.self) { key in
                MapElement(
                    key: key,
                    value: viewModel.state.originalHeaders[key] ?? "",
                    isLoading: viewModel.state.isLoading,
                    isAdded: true,
                    onClickAdd: { key, value in
                        viewModel.onEvent(event: OriginalRouteEvent.UpsertOriginalHeader(key: key, value: value))
                    },
                    onClickRemove: { key in
                        viewModel.onEvent(event: OriginalRouteEvent.RemoveOriginalHeader(key: key))
                    }
                )
                .padding(.horizontal)
            }
            
            // New header input
            MapElement(
                isLoading: viewModel.state.isLoading,
                isAdded: false,
                onClickAdd: { key, value in
                    viewModel.onEvent(event: OriginalRouteEvent.UpsertOriginalHeader(key: key, value: value))
                },
                onClickRemove: { _ in }
            )
            .padding(.horizontal)
        }
    }
    
    private var queriesSection: some View {
        Group {
            Text("Original Queries")
                .font(.headline)
                .foregroundColor(Color.primaryColor)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal)
            
            // Existing queries
            ForEach(Array(viewModel.state.originalQueries.keys.sorted()), id: \.self) { key in
                MapElement(
                    key: key,
                    value: viewModel.state.originalQueries[key] ?? "",
                    isLoading: viewModel.state.isLoading,
                    isAdded: true,
                    onClickAdd: { key, value in
                        viewModel.onEvent(event: OriginalRouteEvent.UpsertOriginalQuery(key: key, value: value))
                    },
                    onClickRemove: { key in
                        viewModel.onEvent(event: OriginalRouteEvent.RemoveOriginalQuery(key: key))
                    }
                )
                .padding(.horizontal)
            }
            
            // New query input
            MapElement(
                isLoading: viewModel.state.isLoading,
                isAdded: false,
                onClickAdd: { key, value in
                    viewModel.onEvent(event: OriginalRouteEvent.UpsertOriginalQuery(key: key, value: value))
                },
                onClickRemove: { _ in }
            )
            .padding(.horizontal)
        }
    }
    
    private var testSection: some View {
        TestColumn(
            isLoading: viewModel.state.isLoading,
            result: result.isEmpty ? "" : "Code: \(code)\nBody:\n\(result)",
            onClick: {
                result = ""
                viewModel.onEvent(event: OriginalRouteEvent.TestOriginalRoute(
                    originalBaseUrl: originalBaseUrl,
                    originalPath: originalPath,
                    originalMethod: originalMethod,
                    originalBody: originalBody
                ))
            }
        )
        .padding(.horizontal)
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
    
    private func handleUiEvent(_ event: OriginalRouteViewModel.UiEvent) {
        switch event {
        case let showError as OriginalRouteViewModel.UiEventShowError:
            errorMessage = showError.message
            showErrorAlert = true
            
        case is OriginalRouteViewModel.UiEventShowOriginalRouteSuccess:
            break
            
        case let showResult as OriginalRouteViewModel.UiEventShowResult:
            code = Int(showResult.code)
            result = showResult.result
            
        case is OriginalRouteViewModel.UiEventNavigateToNextStep:
            // Prepare data for next screen and navigate
            let args = CreateRouteArgs(
                originalResponse: result,
                originalBaseUrl: originalBaseUrl,
                originalPath: originalPath,
                originalMethod: originalMethod,
                originalBody: originalBody,
                originalQueries: viewModel.state.originalQueries,
                originalHeaders: viewModel.state.originalHeaders
            )
            navigate(.fillRouteFields(args))
        default:
            break
        }
    }
}
