//
//  DefaultIconButton.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI

struct DefaultIconButton: View {
    let image: Image
    let contentDescription: String
    let onClick: () -> Void
    var shape: RoundedRectangle = RoundedRectangle(cornerRadius: 24)
    
    var body: some View {
        Button(action: onClick) {
            image
                .resizable()
                .scaledToFit()
                .foregroundColor(Color.onPrimary)
                .padding(12)
        }
        .frame(width: 48, height: 48)
        .background(Color.primaryColor, in: shape)
        .contentShape(shape)
    }
}

#Preview {
    HStack(spacing: 20) {
        DefaultIconButton(
            image: Image(systemName: "heart.fill"),
            contentDescription: "Like",
            onClick: {}
        )
        
        DefaultIconButton(
            image: Image(systemName: "bookmark.fill"),
            contentDescription: "Bookmark",
            onClick: {}
        )
    }
    .padding()
    .background(Color.background)
}
