//
//  NewBodyField.swift
//  iosApp
//
//  Created by Leonardo Bai on 07/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import Shared

struct NewBodyField : Identifiable, Equatable, Hashable {
    let id = UUID()
    let key: String
    let type: String
    
    init(key: String, type: String) {
        self.key = key
        self.type = type
    }
}

extension NewBodyField {
    func toModel() -> Common_sharedNewBodyField {
        return Common_sharedNewBodyField(
            key: self.key,
            type: self.type
        )
    }
    
    static func fromModel(_ model: Common_sharedNewBodyField) -> NewBodyField {
        return NewBodyField(
            key: model.key,
            type: model.type
        )
    }
}
