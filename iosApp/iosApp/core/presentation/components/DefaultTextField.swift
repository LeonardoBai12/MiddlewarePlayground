//
//  DefaultTextField.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI

struct DefaultTextField: View {
    @Binding var text: String
    let label: String
    var icon: Image? = nil
    var isPassword: Bool = false
    var isEnabled: Bool = true
    var isSingleLined: Bool = true
    var keyboardType: UIKeyboardType = .asciiCapable
    var onCommit: () -> Void = {}
    
    @State private var isPasswordVisible: Bool = false
    @FocusState private var isFocused: Bool
    
    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(label)
                .font(.caption)
                .foregroundColor(Color.onSurface.opacity(0.7))
            
            HStack {
                if let icon = icon {
                    icon
                        .foregroundColor(Color.onSurface)
                        .padding(.trailing, 8)
                }
                
                if isPassword && !isPasswordVisible {
                    SecureField("", text: $text)
                        .focused($isFocused)
                } else {
                    TextField("", text: $text)
                        .focused($isFocused)
                }
                
                if !text.isEmpty && isEnabled {
                    Button(action: { text = "" }) {
                        Image(systemName: "xmark.circle.fill")
                            .foregroundColor(Color.onSurface.opacity(0.5))
                    }
                    .buttonStyle(.plain)
                }
                
                if isPassword {
                    Button(action: { isPasswordVisible.toggle() }) {
                        Image(systemName: isPasswordVisible ? "eye.slash" : "eye")
                            .foregroundColor(Color.onSurface)
                    }
                    .buttonStyle(.plain)
                }
            }
            .padding(12)
            .background(Color.surface)
            .cornerRadius(12)
            .overlay(
                RoundedRectangle(cornerRadius: 12)
                    .stroke(isFocused ? Color.primaryColor : Color.onSurface.opacity(0.0), lineWidth: 1)
            )
            .disabled(!isEnabled)
            .opacity(isEnabled ? 1.0 : 0.6)
            .keyboardType(keyboardType)
            .submitLabel(.done)
            .onSubmit {
                isFocused = false
                onCommit()
            }
        }
    }
}

#Preview {
    struct PreviewWrapper: View {
        @State private var text1 = ""
        @State private var text2 = "Editable"
        @State private var password = ""
        
        var body: some View {
            VStack(spacing: 20) {
                DefaultTextField(
                    text: $text1,
                    label: "Username",
                    icon: Image(systemName: "person")
                )
                
                DefaultTextField(
                    text: $text2,
                    label: "Search",
                    isEnabled: false
                )
                
                DefaultTextField(
                    text: $password,
                    label: "Password",
                    isPassword: true,
                    onCommit: { print("Submitted") }
                )
            }
            .padding()
            .background(Color.background)
        }
    }
    
    return PreviewWrapper()
}
