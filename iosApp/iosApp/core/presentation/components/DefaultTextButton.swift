//
//  DefaultTextButton.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI

struct DefaultTextButton: View {
    let text: String
    let onClick: () -> Void
    var enabled: Bool = true
    var icon: Image? = nil
    var containerColor: Color = Color.primaryColor
    var contentColor: Color = Color.onPrimary
    
    var body: some View {
        Button(action: onClick) {
            HStack(spacing: 8) {
                icon?
                    .foregroundColor(contentColor)
                Text(text)
                    .font(.system(size: 18, weight: .semibold))
                    .foregroundColor(contentColor)
            }
            .frame(maxWidth: .infinity)
            .padding(.horizontal, 16)
        }
        .frame(height: 48)
        .background(containerColor)
        .cornerRadius(8)
        .opacity(enabled ? 1.0 : 0.5)
        .disabled(!enabled)
    }
}

#Preview {
    VStack(spacing: 20) {
        DefaultTextButton(
            text: "Continue",
            onClick: {}
        )
        
        DefaultTextButton(
            text: "Sign In",
            onClick: {},
            icon: Image(systemName: "arrow.right")
        )
        
        DefaultTextButton(
            text: "Disabled",
            onClick: {},
            enabled: false
        )
        
        DefaultTextButton(
            text: "Custom",
            onClick: {},
            containerColor: Color.secondary,
            contentColor: Color.onSecondary
        )
    }
    .padding()
    .background(Color.background)
}
