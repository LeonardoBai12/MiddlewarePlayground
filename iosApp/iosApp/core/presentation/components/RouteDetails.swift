//
//  RouteDetails.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI
import Shared

struct RouteDetails: View {
    let originalQueries: [String: String]?
    let originalHeaders: [String: String]?
    let originalBody: String?
    let preConfiguredQueries: [String: String]?
    let preConfiguredHeaders: [String: String]?
    let preConfiguredBody: String?
    
    @State private var showOriginalHeaders = false
    @State private var showOriginalQueries = false
    @State private var showOriginalBody = false
    @State private var showPreConfiguredHeaders = false
    @State private var showPreConfiguredQueries = false
    @State private var showPreConfiguredBody = false
    
    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            // Original Headers
            if let headers = originalHeaders, !headers.isEmpty {
                ToggleSection(
                    title: "Original Headers",
                    isExpanded: $showOriginalHeaders,
                    content: {
                        ForEach(Array(headers.keys), id: \.self) { key in
                            Text("\(key): \(headers[key] ?? "")")
                                .font(.system(size: 16))
                        }
                    }
                )
            }
            
            // Original Queries
            if let queries = originalQueries, !queries.isEmpty {
                ToggleSection(
                    title: "Original Queries",
                    isExpanded: $showOriginalQueries,
                    content: {
                        ForEach(Array(queries.keys), id: \.self) { key in
                            Text("\(key): \(queries[key] ?? "")")
                                .font(.system(size: 16))
                        }
                    }
                )
            }
            
            // Original Body
            if let body = originalBody, isValidBody(body) {
                ToggleSection(
                    title: "Original Body",
                    isExpanded: $showOriginalBody,
                    content: {
                        Text(beautifyJson(body))
                            .font(.system(size: 16))
                    }
                )
            }
            
            // Pre-configured Headers
            if let headers = preConfiguredHeaders, !headers.isEmpty {
                ToggleSection(
                    title: "Pre-configured Headers",
                    isExpanded: $showPreConfiguredHeaders,
                    content: {
                        ForEach(Array(headers.keys), id: \.self) { key in
                            Text("\(key): \(headers[key] ?? "")")
                                .font(.system(size: 16))
                        }
                    }
                )
            }
            
            // Pre-configured Queries
            if let queries = preConfiguredQueries, !queries.isEmpty {
                ToggleSection(
                    title: "Pre-configured Queries",
                    isExpanded: $showPreConfiguredQueries,
                    content: {
                        ForEach(Array(queries.keys), id: \.self) { key in
                            Text("\(key): \(queries[key] ?? "")")
                                .font(.system(size: 16))
                        }
                    }
                )
            }
            
            // Pre-configured Body
            if let body = preConfiguredBody, isValidBody(body) {
                ToggleSection(
                    title: "Pre-configured Body",
                    isExpanded: $showPreConfiguredBody,
                    content: {
                        Text(beautifyJson(body))
                            .font(.system(size: 16))
                    }
                )
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding()
    }
    
    private func isValidBody(_ body: String) -> Bool {
        return !body.isEmpty && body != "{}" && body != "null"
    }
    
    private func beautifyJson(_ json: String) -> String {
        // Implement your JSON beautification logic here
        return json
    }
}

struct ToggleSection<Content: View>: View {
    let title: String
    @Binding var isExpanded: Bool
    @ViewBuilder let content: () -> Content
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Button(action: {
                withAnimation {
                    isExpanded.toggle()
                }
            }) {
                HStack {
                    Text(title)
                        .font(.system(size: 16, weight: .medium))
                        .foregroundColor(Color.primaryColor)
                    
                    Spacer()
                    
                    Image(systemName: isExpanded ? "chevron.down" : "chevron.right")
                        .foregroundColor(Color.primaryColor)
                }
            }
            .buttonStyle(.plain)
            
            if isExpanded {
                VStack(alignment: .leading, spacing: 8) {
                    content()
                }
                .transition(.opacity.combined(with: .move(edge: .top)))
            }
        }
    }
}

#Preview {
    RouteDetails(
        originalQueries: ["param1": "value1", "param2": "value2"],
        originalHeaders: ["Content-Type": "application/json"],
        originalBody: "{\"key\":\"value\"}",
        preConfiguredQueries: ["lang": "en"],
        preConfiguredHeaders: ["Authorization": "Bearer token"],
        preConfiguredBody: "{\"preconfigured\":true}"
    )
    .background(Color.background)
}
