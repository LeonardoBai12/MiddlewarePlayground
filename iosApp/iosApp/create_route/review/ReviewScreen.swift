//
//  ReviewScreen.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct ReviewScreen: View {
    private var args: CreateRouteArgs
    private var createNewRouteUseCase: Middleware_domainCreateNewRouteUseCase
    @ObservedObject private var viewModel: IOSReviewViewModel
    
    @State private var result = ""
    @State private var isFinished = false
    @State private var showErrorAlert = false
    @State private var errorMessage = ""
    @State private var eventSubscription: StateDisposableHandle?
    @Environment(\.navigate) private var navigate
    @Environment(\.replace) private var replace
    
    init(
        args: CreateRouteArgs,
        createNewRouteUseCase: Middleware_domainCreateNewRouteUseCase
    ) {
        self.args = args
        self.createNewRouteUseCase = createNewRouteUseCase
        self.viewModel = IOSReviewViewModel(
            createNewRouteUseCase: createNewRouteUseCase
        )
    }
    
    var body: some View {
        ScrollView {
            VStack(spacing: 16) {
                originalBaseUrlSection
                originalPathSection
                mappedPathSection
                methodsSection
                routeDetailsSection
                testSection
            }
            .padding(.vertical)
        }
        .onAppear { setupScreen() }
        .onDisappear { cleanup() }
        .background(Color.background)
        .toolbar {
            if isFinished {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(
                        action: {
                            replace(.routeListing)
                        }
                    ) {
                        Text(isFinished ? "Done" : "Back")
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
        .navigationTitle("Step 5/5: Review & Create Route")
        .navigationBarBackButtonHidden(isFinished)
    }
    
    // MARK: - Subviews
    
    private var originalBaseUrlSection: some View {
        VStack(alignment: .leading) {
            Text("Original Base URL")
                .font(.system(size: 16))
                .foregroundColor(Color.primaryColor)
            
            Text(args.originalBaseUrl.components(separatedBy: "://").last ?? "")
                .font(.system(size: 16))
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.horizontal)
    }
    
    private var originalPathSection: some View {
        VStack(alignment: .leading) {
            Text("Original Path")
                .font(.system(size: 16))
                .foregroundColor(Color.primaryColor)
            
            Text(args.originalPath.components(separatedBy: "://").last ?? "")
                .font(.system(size: 16))
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.horizontal)
    }
    
    private var mappedPathSection: some View {
        VStack(alignment: .leading) {
            Text("Mapped Path")
                .font(.system(size: 16))
                .foregroundColor(Color.primaryColor)
            
            Text(args.mappedPath.components(separatedBy: "://").last ?? "")
                .font(.system(size: 16))
                .lineLimit(1)
                .truncationMode(.tail)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.horizontal)
    }
    
    private var methodsSection: some View {
        HStack(spacing: 24) {
            MethodBox(
                method: args.originalMethod,
                text: "Original Method"
            )
            MethodBox(
                method: args.mappedMethod,
                text: "Mapped Method"
            )
            Spacer()
        }
        .padding(.horizontal)
    }
    
    private var routeDetailsSection: some View {
        RouteDetails(
            originalQueries: args.originalQueries,
            originalHeaders: args.originalHeaders,
            originalBody: args.originalBody,
            preConfiguredQueries: args.preConfiguredQueries,
            preConfiguredHeaders: args.preConfiguredHeaders,
            preConfiguredBody: args.preConfiguredBody
        )
    }
    
    private var testSection: some View {
        TestColumn(
            isLoading: viewModel.state.isLoading,
            result: result,
            isFinished: isFinished,
            idleText: "Create Route",
            progressText: "Creating Route",
            onClick: {
                result = ""
                viewModel.onEvent(event: ReviewEvent.CreateMappedRoute(
                    originalBaseUrl: args.originalBaseUrl,
                    originalPath: args.originalPath,
                    originalMethod: args.originalMethod,
                    originalBody: args.originalBody,
                    originalQueries: args.originalQueries,
                    originalHeaders: args.originalHeaders,
                    newBodyFields: args.newBodyFields.mapValues { $0.toModel() },
                    oldBodyFields: args.oldBodyFields.mapValues { $0.toModel() },
                    preConfiguredQueries: args.preConfiguredQueries,
                    preConfiguredHeaders: args.preConfiguredHeaders,
                    mappedPath: args.mappedPath,
                    mappedMethod: args.mappedMethod,
                    preConfiguredBody: args.preConfiguredBody,
                    ignoreEmptyValues: args.ignoreEmptyFields
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
    
    private func handleUiEvent(_ event: ReviewViewModel.UiEvent) {
        switch event {
        case let showError as ReviewViewModel.UiEventShowError:
            errorMessage = showError.message
            showErrorAlert = true
            
        case let showResult as ReviewViewModel.UiEventShowCreatedRouteMessage:
            result = showResult.result
            isFinished = true
            
        default:
            break
        }
    }
}
