//
//  SignUpScreen.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct SignUpScreen: View {
    @Environment(\.navigate) private var navigate

    var body: some View {
        ZStack {
            Color.black
                .overlay(
                    Image("sign_up_background")
                        .resizable()
                        .scaledToFill()
                        .opacity(0.8)
                )
                .edgesIgnoringSafeArea(.all)
            
            LinearGradient(
                gradient: Gradient(
                    colors: [
                        Color.black,
                        Color.clear,
                        Color.black
                    ]
                ),
                startPoint: .top,
                endPoint: .bottom
            )
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .edgesIgnoringSafeArea(.all)
            
            VStack(alignment: .leading) {
                VStack(alignment: .leading, spacing: 12) {
                    MiddlewareLogoIcon()
                    Text("Middleware")
                        .font(.system(size: 40, weight: .bold))
                        .foregroundColor(.white)
                }
                .padding(.top, 48)
                .padding(.leading, 48)
                
                Spacer()
                
                VStack(spacing: 16) {
                    DefaultTextButton(
                        text: "Login",
                        onClick: {
                            navigate(.login)
                        }
                    )
                    .padding(.horizontal, 36)
                    
                    DefaultTextButton(
                        text: "Sign Up",
                        onClick: {
                            navigate(.signIn)
                        },
                        containerColor: Color.surface,
                        contentColor: Color.onSurface
                    )
                    .padding(.horizontal, 36)
                }
                .frame(maxWidth: .infinity)
                .padding(.bottom, 36)
            }
        }
    }
}
