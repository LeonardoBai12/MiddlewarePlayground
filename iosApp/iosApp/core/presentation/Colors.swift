//
//  Colors.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

extension Color {
    init(hex: Int64, alpha: Double = 1) {
        self.init(
            .sRGB,
            red: Double((hex & 0xFF0000) >> 16) / 255.0,
            green: Double((hex & 0x00FF00) >> 8) / 255.0,
            blue: Double(hex & 0x0000FF) / 255.0,
            opacity: alpha
        )
    }
    
    private static let playgroundColors = PlaygroundColors()
    
    // Dark colors
    static let darkBlue = Color(hex: playgroundColors.DarkBlue)
    static let darkGrayBlue = Color(hex: playgroundColors.DarkGrayBlue)
    
    // Pink colors
    static let primaryPink = Color(hex: playgroundColors.PrimaryPink)
    static let primaryDarkPink = Color(hex: playgroundColors.PrimaryDarkPink)
    
    // Blue colors
    static let backgroundLightBlue = Color(hex: playgroundColors.BackgroundLightBlue)
    static let surfaceBlue = Color(hex: playgroundColors.SurfaceBlue)
    
    // Other colors
    static let secondaryPurple = Color(hex: playgroundColors.SecondaryPurple)
    static let tertiaryYellow = Color(hex: playgroundColors.TertiaryYellow)
    static let buttonGreen = Color(hex: playgroundColors.ButtonGreen)
    
    // Theme colors
    static let primaryColor = Color(light: .primaryDarkPink, dark: .primaryPink)
    static let background = Color(light: .backgroundLightBlue, dark: .darkBlue)
    static let onPrimary = Color(light: .white, dark: .darkGrayBlue)
    static let onBackground = Color(light: .primaryDarkPink, dark: .primaryPink)
    static let surface = Color(light: .surfaceBlue, dark: .darkGrayBlue)
    static let onSurface = Color(light: .primaryDarkPink, dark: .primaryPink)
    static let secondary = Color(light: .secondaryPurple, dark: .secondaryPurple)
    static let onSecondary = Color(light: .primaryPink, dark: .primaryPink)
    static let tertiary = Color(light: .tertiaryYellow, dark: .tertiaryYellow)
    static let onTertiary = Color(light: .darkGrayBlue, dark: .darkGrayBlue)
}

private struct PlaygroundColors {
    let DarkBlue: Int64 = 0xFF030826
    let DarkGrayBlue: Int64 = 0xFF18163A
    let PrimaryPink: Int64 = 0xFFF544B1
    let PrimaryDarkPink: Int64 = 0xFFCB0686
    let BackgroundLightBlue: Int64 = 0xFFD1D7F3
    let SurfaceBlue: Int64 = 0xFFBDC6F3
    let SecondaryPurple: Int64 = 0xFF4F00CE
    let TertiaryYellow: Int64 = 0xFFFFC107
    let ButtonGreen: Int64 = 0xFF00C853
}

private extension Color {
    init(light: Self, dark: Self) {
        self.init(uiColor: UIColor(light: UIColor(light), dark: UIColor(dark)))
    }
}

private extension UIColor {
    convenience init(light: UIColor, dark: UIColor) {
        self.init { traits in
            switch traits.userInterfaceStyle {
            case .unspecified, .light:
                return light
            case .dark:
                return dark
            @unknown default:
                return light
            }
        }
    }
}
