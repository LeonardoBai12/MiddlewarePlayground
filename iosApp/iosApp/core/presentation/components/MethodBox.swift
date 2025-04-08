//
//  MethodBox.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI
import Shared

struct MethodBox: View {
    let method: Common_sharedMiddlewareHttpMethods
    let text: String?
    
    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            if let text = text {
                Text(text)
                    .font(.system(size: 16))
                    .foregroundColor(Color.primaryColor)
            }
            
            Text(method.name)
                .font(.system(size: 16, weight: .medium))
                .foregroundColor(.white)
                .padding(.vertical, 6)
                .padding(.horizontal, 12)
                .background(Color(hex: method.color))
                .cornerRadius(12)
        }
    }
}

#Preview {
    VStack(spacing: 16) {
        MethodBox(method: .get, text: "Request Method")
        MethodBox(method: .post, text: "aaaa")
        MethodBox(method: .patch, text: "Partial Update")
        MethodBox(method: .put, text: "Full Update")
        MethodBox(method: .delete_, text: "Remove Resource")
        MethodBox(method: .head, text: "Header")
    }
    .padding()
    .background(Color.background)
}
