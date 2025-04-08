//
//  MappedRoute.swift
//  iosApp
//
//  Created by Leonardo Bai on 07/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI
import Shared

struct MappedRoute: Identifiable, Equatable, Hashable {
    let id: String
    let path: String
    let method: Common_sharedMiddlewareHttpMethods
    let originalBaseUrl: String
    let originalPath: String
    let originalMethod: Common_sharedMiddlewareHttpMethods
    let originalQueries: [String: String]
    let originalHeaders: [String: String]
    let originalBody: String?
    let preConfiguredQueries: [String: String]
    let preConfiguredHeaders: [String: String]
    let preConfiguredBody: String?
    let isFavourite: Bool

    init(
        uuid: String,
        path: String,
        method: Common_sharedMiddlewareHttpMethods,
        originalBaseUrl: String,
        originalPath: String,
        originalMethod: Common_sharedMiddlewareHttpMethods,
        originalQueries: [String: String] = [:],
        originalHeaders: [String: String] = [:],
        originalBody: String? = nil,
        preConfiguredQueries: [String: String] = [:],
        preConfiguredHeaders: [String: String] = [:],
        preConfiguredBody: String? = nil,
        isFavourite: Bool
    ) {
        self.id = uuid
        self.path = path
        self.method = method
        self.originalBaseUrl = originalBaseUrl
        self.originalPath = originalPath
        self.originalMethod = originalMethod
        self.originalQueries = originalQueries
        self.originalHeaders = originalHeaders
        self.originalBody = originalBody
        self.preConfiguredQueries = preConfiguredQueries
        self.preConfiguredHeaders = preConfiguredHeaders
        self.preConfiguredBody = preConfiguredBody
        self.isFavourite = isFavourite
    }
    
    func toModel() -> Common_sharedMappedRoute {
        return Common_sharedMappedRoute(uuid: id, path: path, method: method, originalBaseUrl: originalBaseUrl, originalPath: originalPath, originalMethod: originalMethod, originalQueries: originalQueries, originalHeaders: originalHeaders, originalBody: originalBody, preConfiguredQueries: preConfiguredQueries, preConfiguredHeaders: preConfiguredHeaders, preConfiguredBody: preConfiguredBody, isFavourite: isFavourite)
    }	
}

extension MappedRoute {
    init(from model: Common_sharedMappedRoute) {
        self.init(
            uuid: model.uuid,
            path: model.path,
            method: model.method,
            originalBaseUrl: model.originalBaseUrl,
            originalPath: model.originalPath,
            originalMethod: model.originalMethod,
            originalQueries: model.originalQueries,
            originalHeaders: model.originalHeaders,
            originalBody: model.originalBody,
            preConfiguredQueries: model.preConfiguredQueries,
            preConfiguredHeaders: model.preConfiguredHeaders,
            preConfiguredBody: model.preConfiguredBody,
            isFavourite: model.isFavourite
        )
    }
}
