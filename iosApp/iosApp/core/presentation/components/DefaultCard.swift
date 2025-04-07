//
//  DefaultCard.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI

struct DefaultCard<Content: View>: View {
    let onClick: () -> Void
    let content: Content
    
    init(
        onClick: @escaping () -> Void,
        @ViewBuilder content: () -> Content
    ) {
        self.onClick = onClick
        self.content = content()
    }
    
    var body: some View {
        Button(action: onClick) {
            content
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding()
                .background(Color.surface)
                .foregroundColor(Color.onSurface)
                .cornerRadius(16)
                .shadow(color: Color.primaryColor.opacity(0.1), radius: 4, x: 0, y: 2)
        }
        .buttonStyle(.plain)
    }
}

#Preview {
    VStack(spacing: 20) {
        DefaultCard(onClick: {}) {
            Text("Default Card")
                .font(.headline)
        }
        
        DefaultCard(onClick: {}) {
            VStack(alignment: .leading, spacing: 8) {
                Text("Card with Content")
                    .font(.headline)
                Text("This card uses your custom color system")
                    .font(.subheadline)
                HStack {
                    Circle()
                        .fill(Color.primaryColor)
                        .frame(width: 20, height: 20)
                    Circle()
                        .fill(Color.secondary)
                        .frame(width: 20, height: 20)
                    Circle()
                        .fill(Color.tertiary)
                        .frame(width: 20, height: 20)
                }
            }
        }
    }
    .padding()
    .background(Color.background)
    .previewDisplayName("Cards on Background")
}
