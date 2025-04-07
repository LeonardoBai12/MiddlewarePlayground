//
//  MiddlewareLogoIcon.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//


import SwiftUI

struct MiddlewareLogoIcon: View {
    var size: CGSize = CGSize(width: 64, height: 64)
    
    var body: some View {
        DefaultIcon(
            size: size,
            image: Image("ProjectMiddlewareLogo_NoBG", bundle: .main),
            containerColor: Color.surface,
            contentColor: Color.onSurface,
            contentDescription: "Middleware Logo"
        )
    }
}

struct DefaultIcon: View {
    var size: CGSize = CGSize(width: 48, height: 48)
    var image: Image
    var containerColor: Color = Color.onPrimary
    var contentColor: Color = Color.primaryColor
    var contentDescription: String
    var cornerRadius: CGFloat = 24
    
    var body: some View {
        ZStack {
            containerColor
                .frame(width: size.width, height: size.height)
                .cornerRadius(cornerRadius)
            
            image
                .resizable()
                .scaledToFit()
                .foregroundColor(contentColor)
                .frame(width: size.width * 0.7, height: size.height * 0.7)
        }
        .accessibilityLabel(contentDescription)
        .frame(width: size.width, height: size.height)
    }
}

#Preview {
    VStack(spacing: 20) {
        MiddlewareLogoIcon()
        
        DefaultIcon(
            image: Image(systemName: "heart.fill"),
            contentDescription: "Heart Icon"
        )
        
        DefaultIcon(
            size: CGSize(width: 80, height: 80),
            image: Image(systemName: "star.fill"),
            containerColor: Color.secondary,
            contentColor: Color.onSecondary,
            contentDescription: "Star Icon",
            cornerRadius: 16
        )
    }
    .padding()
    .background(Color.background)
}
