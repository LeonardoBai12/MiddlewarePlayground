//
//  OldBodyField.swift
//  iosApp
//
//  Created by Leonardo Bai on 07/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import Shared

struct OldBodyField : Identifiable, Equatable, Hashable {
    let id = UUID()
    let keys: [String]
    let type: String
    let parents: [String]
    
    init(keys: [String], type: String, parents: [String] = []) {
        self.keys = keys
        self.type = type
        self.parents = parents
    }
}

extension OldBodyField {
    func toModel() -> Common_sharedOldBodyField {
        return Common_sharedOldBodyField(
            keys: self.keys,
            type: self.type,
            parents: self.parents
        )
    }
    
    static func frommodel(_ model: Common_sharedOldBodyField) -> OldBodyField {
        return OldBodyField(
            keys: model.keys,
            type: model.type,
            parents: model.parents
        )
    }
}
