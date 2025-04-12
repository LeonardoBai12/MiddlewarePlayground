//
//  FillPreConfigsScreen.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct FillPreConfigsScreen: View {
    @ObservedObject private var viewModel: IOSFillPreConfigsViewModel
    private let args: CreateRouteArgs
    
    @State private var mappedPath: String
    @State private var mappedMethodExpanded = false
    @State private var mappedMethod: Common_sharedMiddlewareHttpMethods
    @State private var mappedBody: String
    @State private var ignoreEmptyFields: Bool
    
    @State private var showErrorAlert = false
    @State private var errorMessage = ""
    @State private var eventSubscription: StateDisposableHandle?
    @Environment(\.navigate) private var navigate
    
    init(args: CreateRouteArgs) {
        self.args = args
        self._viewModel = ObservedObject(wrappedValue: IOSFillPreConfigsViewModel())
        self._mappedPath = State(initialValue: args.mappedPath)
        self._mappedMethod = State(initialValue: args.mappedMethod)
        self._mappedBody = State(initialValue: args.preConfiguredBody)
        self._ignoreEmptyFields = State(initialValue: args.ignoreEmptyFields)
    }
    
    var body: some View {
        ScrollView {
            VStack(spacing: 16) {
                methodAndToggleSection
                mappedPathSection
                mappedBodySection
                preConfiguredHeadersSection
                preConfiguredQueriesSection
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
                        if mappedPath.isEmpty {
                            errorMessage = "Please fill the mapped path"
                            showErrorAlert = true
                            return
                        }
                        
                        let newArgs = CreateRouteArgs(
                            originalResponse: args.originalResponse,
                            originalBaseUrl: args.originalBaseUrl,
                            originalPath: args.originalPath,
                            originalMethod: args.originalMethod,
                            originalBody: args.originalBody,
                            originalQueries: args.originalQueries,
                            originalHeaders: args.originalHeaders,
                            oldBodyFields: args.oldBodyFields,
                            newBodyFields: args.newBodyFields,
                            mappedPath: mappedPath,
                            mappedMethod: mappedMethod,
                            ignoreEmptyFields: ignoreEmptyFields,
                            preConfiguredBody: mappedBody,
                            preConfiguredQueries: viewModel.state.preConfiguredQueries,
                            preConfiguredHeaders: viewModel.state.preConfiguredHeaders
                        )
                        
                        navigate(.preview(newArgs))
                    }
                ) {
                    HStack {
                        Text("Next")
                            .foregroundColor(Color.primaryColor)
                        Image(systemName: "chevron.right")
                            .foregroundColor(Color.primaryColor)
                    }
                }
            }
        }
        .alert("Error", isPresented: $showErrorAlert) {
            Button("OK", role: .cancel) { }
        } message: {
            Text(errorMessage)
        }
        .navigationTitle("Step 3/5: Fill Pre Configs")
    }
    
    // MARK: - Subviews
    
    private var methodAndToggleSection: some View {
        HStack {
            MethodBox(
                method: mappedMethod,
                text: "Mapped Method"
            )
            .onTapGesture {
                mappedMethodExpanded = true
            }
            .confirmationDialog(
                "Select Method",
                isPresented: $mappedMethodExpanded,
                titleVisibility: .visible
            ) {
                ForEach(Common_sharedMiddlewareHttpMethods.entries, id: \.self) { method in
                    Button(method.name) {
                        mappedMethod = method
                    }
                }
            }
            
            Spacer()
            
            VStack(alignment: .leading) {
                Text("Ignore Empty Fields")
                    .font(.system(size: 16))
                    .foregroundColor(Color.primaryColor)
                Toggle("", isOn: $ignoreEmptyFields)
                    .labelsHidden()
            }
        }
        .padding(.horizontal)
    }
    
    private var mappedPathSection: some View {
        DefaultTextField(
            text: $mappedPath,
            label: "Mapped Path",
            isEnabled: true
        )
        .padding(.horizontal)
    }
    
    private var mappedBodySection: some View {
        DefaultTextField(
            text: $mappedBody,
            label: "Pre-configured Body",
            isEnabled: true,
            isSingleLined: false
        )
        .padding(.horizontal)
    }
    
    private var preConfiguredHeadersSection: some View {
        VStack {
            Text("Pre-configured Headers")
                .font(.headline)
                .foregroundColor(Color.primaryColor)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal)
            
            // Existing headers
            ForEach(viewModel.state.preConfiguredHeaders.keys.sorted(), id: \.self) { key in
                MapElement(
                    key: key,
                    value: viewModel.state.preConfiguredHeaders[key] ?? "",
                    isLoading: false,
                    isAdded: true,
                    onClickAdd: { key, value in
                        viewModel.onEvent(event: FillPreConfigsEvent.UpsertPreConfiguredHeader(key: key, value: value))
                    },
                    onClickRemove: { key in
                        viewModel.onEvent(event: FillPreConfigsEvent.RemovePreConfiguredHeader(key: key))
                    }
                )
                .padding(.horizontal)
            }
            
            // New header input
            MapElement(
                isLoading: false,
                isAdded: false,
                onClickAdd: { key, value in
                    viewModel.onEvent(event: FillPreConfigsEvent.UpsertPreConfiguredHeader(key: key, value: value))
                },
                onClickRemove: { _ in }
            )
            .padding(.horizontal)
        }
    }
    
    private var preConfiguredQueriesSection: some View {
        VStack {
            Text("Pre-configured Queries")
                .font(.headline)
                .foregroundColor(Color.primaryColor)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal)
            
            // Existing queries
            ForEach(viewModel.state.preConfiguredQueries.keys.sorted(), id: \.self) { key in
                MapElement(
                    key: key,
                    value: viewModel.state.preConfiguredQueries[key] ?? "",
                    isLoading: false,
                    isAdded: true,
                    onClickAdd: { key, value in
                        viewModel.onEvent(event: FillPreConfigsEvent.UpsertPreConfiguredQuery(key: key, value: value))
                    },
                    onClickRemove: { key in
                        viewModel.onEvent(event: FillPreConfigsEvent.RemovePreConfiguredQuery(key: key))
                    }
                )
                .padding(.horizontal)
            }
            
            // New query input
            MapElement(
                isLoading: false,
                isAdded: false,
                onClickAdd: { key, value in
                    viewModel.onEvent(event: FillPreConfigsEvent.UpsertPreConfiguredQuery(key: key, value: value))
                },
                onClickRemove: { _ in }
            )
            .padding(.horizontal)
        }
    }
    
    private func setupScreen() {
        viewModel.startObserving()
        subscribeToEvents()
        
        if viewModel.state.preConfiguredHeaders.isEmpty {
            viewModel.onEvent(event: FillPreConfigsEvent.UpsertPreConfiguredHeader(key: "Content-Type", value: "application/json"))
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
    
    private func handleUiEvent(_ event: FillPreConfigsViewModel.UiEvent) {
        if let showError = event as? FillPreConfigsViewModel.UiEventShowError {
            errorMessage = showError.message
            showErrorAlert = true
        }
    }
}
