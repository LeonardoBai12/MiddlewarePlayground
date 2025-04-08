//
//  MapElement.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI

struct MapElement: View {
    @State private var mapKey: String
    @State private var mapValue: String
    let isLoading: Bool
    let isAdded: Bool
    let onClickAdd: (String, String) -> Void
    let onClickRemove: (String) -> Void
    
    init(
        key: String = "",
        value: String = "",
        isLoading: Bool,
        isAdded: Bool,
        onClickAdd: @escaping (String, String) -> Void,
        onClickRemove: @escaping (String) -> Void
    ) {
        self._mapKey = State(initialValue: key)
        self._mapValue = State(initialValue: value)
        self.isLoading = isLoading
        self.isAdded = isAdded
        self.onClickAdd = onClickAdd
        self.onClickRemove = onClickRemove
    }
    
    var body: some View {
        HStack(alignment: .center, spacing: 8) {
            // Key TextField - 35% width
            DefaultTextField(
                text: $mapKey,
                label: "Key",
                isEnabled: !isLoading && !isAdded
            )
            .frame(width: UIScreen.main.bounds.width * 0.40)
            
            // Value TextField - 50% width
            DefaultTextField(
                text: $mapValue,
                label: "Value",
                isEnabled: !isLoading && !isAdded
            )
            .frame(width: UIScreen.main.bounds.width * 0.40)
            
            // Add/Remove Button
            Button(action: {
                if isAdded {
                    onClickRemove(mapKey)
                } else {
                    onClickAdd(mapKey, mapValue)
                    mapKey = ""
                    mapValue = ""
                }
            }) {
                Image(systemName: isAdded ? "minus" : "plus")
                    .font(.system(size: 18, weight: .medium))
                    .foregroundColor(Color.primaryColor)
                    .frame(width: 24, height: 24)
                    .padding(8)
                    .background(Color.surface)
                    .clipShape(Circle())
            }
            .disabled(isLoading || (isAdded ? mapKey.isEmpty : (mapKey.isEmpty || mapValue.isEmpty)))
            .padding(.top, 16)
            .buttonStyle(.plain)
        }
        .frame(maxWidth: .infinity)
    }
}

#Preview {
    VStack(spacing: 12) {
        MapElement(
            key: "param1",
            value: "value1",
            isLoading: false,
            isAdded: true,
            onClickAdd: { _, _ in },
            onClickRemove: { _ in }
        )
        
        MapElement(
            key: "param2",
            value: "",
            isLoading: false,
            isAdded: false,
            onClickAdd: { _, _ in },
            onClickRemove: { _ in }
        )
        
        MapElement(
            isLoading: false,
            isAdded: false,
            onClickAdd: { _, _ in },
            onClickRemove: { _ in }
        )
    }
    .padding()
    .background(Color.background)
}
