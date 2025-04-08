//
//  CreateRouteArgs.swift
//  iosApp
//
//  Created by Leonardo Bai on 07/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import Shared

struct CreateRouteArgs : Identifiable, Equatable, Hashable {
    let id = UUID()
    let originalResponse: String
    let originalBaseUrl: String
    let originalPath: String
    let originalMethod: Common_sharedMiddlewareHttpMethods
    let originalBody: String
    let originalQueries: [String: String]
    let originalHeaders: [String: String]
    let oldBodyFields: [String: OldBodyField]
    let newBodyFields: [String: NewBodyField]
    let mappedPath: String
    let mappedMethod: Common_sharedMiddlewareHttpMethods
    let ignoreEmptyFields: Bool
    let preConfiguredBody: String
    let preConfiguredQueries: [String: String]
    let preConfiguredHeaders: [String: String]
    
    init(
        originalResponse: String,
        originalBaseUrl: String,
        originalPath: String,
        originalMethod: Common_sharedMiddlewareHttpMethods = .get,
        originalBody: String = "",
        originalQueries: [String: String] = [:],
        originalHeaders: [String: String] = [:],
        oldBodyFields: [String: OldBodyField] = [:],
        newBodyFields: [String: NewBodyField] = [:],
        mappedPath: String? = nil,
        mappedMethod: Common_sharedMiddlewareHttpMethods? = nil,
        ignoreEmptyFields: Bool = true,
        preConfiguredBody: String = "",
        preConfiguredQueries: [String: String] = [:],
        preConfiguredHeaders: [String: String] = ["Content-Type": "application/json"]
    ) {
        self.originalResponse = originalResponse
        self.originalBaseUrl = originalBaseUrl
        self.originalPath = originalPath
        self.originalMethod = originalMethod
        self.originalBody = originalBody
        self.originalQueries = originalQueries
        self.originalHeaders = originalHeaders
        self.oldBodyFields = oldBodyFields
        self.newBodyFields = newBodyFields
        self.mappedPath = mappedPath ?? originalPath
        self.mappedMethod = mappedMethod ?? originalMethod
        self.ignoreEmptyFields = ignoreEmptyFields
        self.preConfiguredBody = preConfiguredBody
        self.preConfiguredQueries = preConfiguredQueries
        self.preConfiguredHeaders = preConfiguredHeaders
    }
}
