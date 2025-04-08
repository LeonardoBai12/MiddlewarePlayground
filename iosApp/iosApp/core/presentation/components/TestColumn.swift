//
//  TestColumn.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI
import Shared

struct TestColumn: View {
    let isLoading: Bool
    let result: String
    let isFinished: Bool
    let idleText: String?
    let progressText: String?
    let onClick: () -> Void
    
    @State private var showCopyConfirmation = false
    
    init(
        isLoading: Bool,
        result: String,
        isFinished: Bool = false,
        idleText: String? = nil,
        progressText: String? = nil,
        onClick: @escaping () -> Void
    ) {
        self.isLoading = isLoading
        self.result = result
        self.isFinished = isFinished
        self.idleText = idleText
        self.progressText = progressText
        self.onClick = onClick
    }
    
    var body: some View {
        VStack(alignment: .center, spacing: 12) {
            // Test Button
            Button(action: onClick) {
                HStack {
                    Image(systemName: "play.fill")
                    Text(buttonText)
                }
                .frame(maxWidth: .infinity)
                .padding()
            }
            .disabled(isLoading || isFinished)
            .background(isLoading || isFinished ? Color.gray : Color.buttonGreen)
            .foregroundColor(.white)
            .cornerRadius(8)
            .frame(maxWidth: UIScreen.main.bounds.width * 0.7)
            
            // Loading Indicator
            if isLoading && !isFinished {
                ProgressView()
            }
            
            // Results Section
            if !result.isEmpty {
                VStack(alignment: .leading) {
                    HStack {
                        Text("Result")
                            .font(.system(size: 16))
                            .foregroundColor(Color.primaryColor)
                        
                        Spacer()
                        
                        Button(action: copyToClipboard) {
                            Image(systemName: "doc.on.doc")
                                .foregroundColor(Color.primaryColor)
                        }
                        .alert("Copied to clipboard", isPresented: $showCopyConfirmation) {
                            Button("OK", role: .cancel) {}
                        }
                    }
                    
                    Text(result)
                        .font(.system(size: 16))
                        .textSelection(.enabled)
                }
            }
        }
        .padding(.top, 16)
        .frame(maxWidth: .infinity)
    }
    
    private var buttonText: String {
        if isLoading {
            return progressText ?? "Testing Route"
        } else {
            return idleText ?? "Test Route"
        }
    }
    
    private func copyToClipboard() {
        UIPasteboard.general.string = result
        showCopyConfirmation = true
    }
}

#Preview {
    VStack {
        TestColumn(
            isLoading: false,
            result: "",
            onClick: {}
        )
        
        TestColumn(
            isLoading: true,
            result: "",
            progressText: "Processing...",
            onClick: {}
        )
        
        TestColumn(
            isLoading: false,
            result: "{\"status\":\"success\",\"data\":{\"id\":123}}",
            isFinished: true,
            idleText: "Run Again",
            onClick: {}
        )
    }
    .padding()
    .background(Color.background)
}
