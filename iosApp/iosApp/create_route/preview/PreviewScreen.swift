//
//  PreviewScreen.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct PreviewScreen: View {
    private var args: CreateRouteArgs
    private var requestPreviewUseCase: Middleware_domainRequestPreviewUseCase
    @ObservedObject private var viewModel: IOSPreviewViewModel
    
    @State private var result = ""
    @State private var showErrorAlert = false
    @State private var errorMessage = ""
    @State private var eventSubscription: StateDisposableHandle?
    @Environment(\.navigate) private var navigate
    
    init(
        args: CreateRouteArgs,
        requestPreviewUseCase: Middleware_domainRequestPreviewUseCase
    ) {
        self.args = args
        self.requestPreviewUseCase = requestPreviewUseCase
        self.viewModel = IOSPreviewViewModel(
            requestPreviewUseCase: requestPreviewUseCase
        )
    }
    
    var body: some View {
        ScrollView {
            VStack(spacing: 16) {
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
                        if result.isEmpty {
                            errorMessage = "Please test the route before proceeding"
                            showErrorAlert = true
                            return
                        }
                        
                        navigate(.review(args))
                    }
                ) {
                    HStack {
                        Text("Next")
                            .foregroundColor(Color.primaryColor)
                        Image(systemName: "chevron.right")
                            .foregroundColor(Color.primaryColor)
                    }
                }
                .disabled(viewModel.state.isLoading)
            }
        }
        .alert("Error", isPresented: $showErrorAlert) {
            Button("OK", role: .cancel) { }
        } message: {
            Text(errorMessage)
        }
        .navigationTitle("Step 4/5: Preview")
    }
    
    // MARK: - Subviews
    
    private var testSection: some View {
        TestColumn(
            isLoading: viewModel.state.isLoading,
            result: result,
            onClick: {
                result = ""
                viewModel.onEvent(event: PreviewEvent.RequestPreview(
                    response: args.originalResponse,
                    newBodyFields: args.newBodyFields.mapValues { $0.toModel() },
                    oldBodyFields: args.oldBodyFields.mapValues { $0.toModel() },
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
    
    private func handleUiEvent(_ event: PreviewViewModel.UiEvent) {
        switch event {
        case let showError as PreviewViewModel.UiEventShowError:
            errorMessage = showError.message
            showErrorAlert = true
            
        case let showResult as PreviewViewModel.UiEventShowResult:
            result = showResult.result
            
        default:
            break
        }
    }
}
