//
//  FillRouteScreen.swift
//  iosApp
//
//  Created by Leonardo Bai on 06/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct FillRouteFieldsScreen: View {
    @ObservedObject private var viewModel: IOSFillRouteFieldsViewModel
    private let args: CreateRouteArgs?
    
    @State private var showErrorAlert = false
    @State private var errorMessage = ""
    @State private var eventSubscription: StateDisposableHandle?
    @Environment(\.navigate) private var navigate
    
    // State for single field mapping
    @State private var newSingleFieldName = ""
    @State private var selectedSingleField = ""
    
    // State for field grouping
    @State private var newGroupName = ""
    @State private var selectedFieldsForGroup: Set<String> = []
    
    // State for field concatenation
    @State private var newConcatenationName = ""
    @State private var selectedPairsForConcatenation: [Pair] = []
    @State private var firstConcatField = ""
    @State private var secondConcatField = ""
    
    private var oldBodyFieldsKeys: [String]
    
    struct Pair: Hashable {
        let first: String
        let second: String
    }
    
    init(
        args: CreateRouteArgs?
    ) {
        self.args = args
        self.viewModel = IOSFillRouteFieldsViewModel()
        self.oldBodyFieldsKeys = Array(Set(args?.oldBodyFields.values.flatMap { $0.keys } ?? []))
    }
    
    var body: some View {
        ScrollView {
            VStack(spacing: 24) {
                // 1. Single Field Mapping
                singleFieldMappingSection
                
                // 2. Field Grouping
                fieldGroupingSection
                
                // 3. Field Concatenation
                fieldConcatenationSection
                
                // 4. Existing Mappings
                existingMappingsSection
            }
            .padding()
        }
        .background(Color.background)
        .navigationTitle("Step 2/5: Map Response Fields")
        .toolbar {
            ToolbarItem(placement: .navigationBarTrailing) {
                toolbarItem
            }
        }
        .onAppear { setupScreen() }
        .onDisappear { cleanup() }
        .alert("Error", isPresented: $showErrorAlert) {
            Button("OK", role: .cancel) { }
        } message: {
            Text(errorMessage)
        }
    }
    
    // MARK: - Subviews
    
    private var toolbarItem : some View {
        Button(
            action: {
                if viewModel.state.newBodyFields.isEmpty {
                    showErrorAlert = true
                    errorMessage = "Please add at least one mapping"
                } else {
                    let newArgs = CreateRouteArgs(
                        originalResponse: args?.originalResponse ?? "",
                        originalBaseUrl: args?.originalBaseUrl ?? "",
                        originalPath: args?.originalPath ?? "",
                        originalMethod: args?.originalMethod ?? .get,
                        originalBody: args?.originalBody ?? "",
                        originalQueries: args?.originalQueries ?? [:],
                        originalHeaders: args?.originalHeaders ?? [:],
                        oldBodyFields: combineOldBodyFields(
                            originalFields: args?.oldBodyFields ?? [:],
                            newMappings: viewModel.state.oldBodyFields.mapValues({ oldField in
                                OldBodyField.frommodel(oldField)
                            })
                        ),
                        newBodyFields: viewModel.state.newBodyFields.mapValues({ newField in
                            NewBodyField.fromModel(newField)
                        })
                    )
                    navigate(.fillPreConfigs(newArgs))
                }
            }
        ) {
            HStack {
                Text("Next")
                    .foregroundColor(Color.primaryColor)
                Image(systemName: "chevron.right")
                    .foregroundColor(Color.primaryColor)
            }
        }
        .foregroundColor(Color.primaryColor)
    }
    
    private var availableFieldsForSingleMapping: [String] {
        oldBodyFieldsKeys.filter { field in
            !viewModel.state.oldBodyFields.values.contains { $0.keys.contains(field) }
        }
    }
    
    private var availableFieldsForGrouping: [String] {
        oldBodyFieldsKeys.filter { field in
            !viewModel.state.oldBodyFields.values.contains { $0.keys.contains(field) }
        }
    }
    
    private var availableFieldsForConcatenation: [String] {
        oldBodyFieldsKeys.filter { field in
            !viewModel.state.oldBodyFields.values.contains { $0.keys.contains { $0.contains(field) } }
        }
    }
    
    private var singleFieldMappingSection: some View {
        Section(header: sectionHeader("Single Field Mapping")) {
            DefaultTextField(
                text: $newSingleFieldName,
                label: "New Field Name"
            )
            
            FieldSelectorDropdown(
                fields: availableFieldsForSingleMapping,
                selectedField: $selectedSingleField,
                label: "Select source field"
            )
            
            DefaultTextButton(
                text: "Add Single Field",
                onClick: {
                    // Match Kotlin's event structure exactly
                    viewModel.onEvent(event: FillRouteFieldsEvent.UpsertNewBodyField(
                        key: newSingleFieldName,
                        field: NewBodyField(
                            key: newSingleFieldName,
                            type: inferFieldType(selectedSingleField)
                        ).toModel()
                    ))
                    
                    viewModel.onEvent(event: FillRouteFieldsEvent.UpsertOldBodyField(
                        key: newSingleFieldName,
                        field: OldBodyField(
                            keys: [selectedSingleField],
                            type: inferFieldType(selectedSingleField),
                            parents: []
                        ).toModel()
                    ))
                    
                    newSingleFieldName = ""
                    selectedSingleField = ""
                },
                enabled: !newSingleFieldName.isEmpty && !selectedSingleField.isEmpty
            )
        }
    }
    
    private var fieldGroupingSection: some View {
        Section(header: sectionHeader("Field Grouping (Multiple Fields → Array)")) {
            DefaultTextField(
                text: $newGroupName,
                label: "New Group Name"
            )
            
            if availableFieldsForGrouping.isEmpty {
                Text("No fields available for grouping")
                    .font(.caption)
                    .foregroundColor(.gray)
            } else {
                VStack(alignment: .leading) {
                    Text("Select fields to group:")
                        .font(.caption)
                    
                    ForEach(availableFieldsForGrouping, id: \.self) { field in
                        Toggle(isOn: Binding(
                            get: { selectedFieldsForGroup.contains(field) },
                            set: { if $0 { selectedFieldsForGroup.insert(field) }
                                  else { selectedFieldsForGroup.remove(field) } }
                        )) {
                            Text(field)
                        }
                        .toggleStyle(CheckboxToggleStyle())
                        .frame(maxWidth: .infinity, alignment: .leading)
                    }
                }
            }
            
            DefaultTextButton(
                text: "Add Field Group",
                onClick: {
                    viewModel.onEvent(event: FillRouteFieldsEvent.UpsertNewBodyField(
                        key: newGroupName,
                        field: NewBodyField(key: newGroupName, type: "String").toModel()
                    ))
                    
                    viewModel.onEvent(event: FillRouteFieldsEvent.UpsertOldBodyField(
                        key: newGroupName,
                        field: OldBodyField(
                            keys: Array(selectedFieldsForGroup),
                            type: "String",
                            parents: []
                        ).toModel()
                    ))
                    
                    newGroupName = ""
                    selectedFieldsForGroup = []
                },
                enabled: !newGroupName.isEmpty && !selectedFieldsForGroup.isEmpty
            )
        }
    }
    
    private var fieldConcatenationSection: some View {
        Section(header: sectionHeader("Field Concatenation")) {
            DefaultTextField(
                text: $newConcatenationName,
                label: "New Concatenation Name"
            )
            
            HStack {
                FieldSelectorDropdown(
                    fields: availableFieldsForConcatenation,
                    selectedField: $firstConcatField,
                    label: "First field"
                )
                
                FieldSelectorDropdown(
                    fields: availableFieldsForConcatenation.filter { $0 != firstConcatField },
                    selectedField: $secondConcatField,
                    label: "Second field"
                )
            }
            
            if !selectedPairsForConcatenation.isEmpty {
                VStack(alignment: .leading) {
                    Text("Selected pairs:")
                        .font(.caption)
                    
                    ForEach(selectedPairsForConcatenation, id: \.self) { pair in
                        HStack {
                            Text("\(pair.first) + \(pair.second)")
                            Spacer()
                            Button(action: {
                                selectedPairsForConcatenation.removeAll { $0 == pair }
                            }) {
                                Image(systemName: "trash")
                                    .foregroundColor(.red)
                            }
                        }
                    }
                }
            }
            
            HStack {
                DefaultTextButton(
                    text: "Add Pair",
                    onClick: {
                        selectedPairsForConcatenation.append(
                            Pair(first: firstConcatField, second: secondConcatField)
                        )
                        firstConcatField = ""
                        secondConcatField = ""
                    },
                    enabled: !firstConcatField.isEmpty && !secondConcatField.isEmpty
                )
                
                DefaultTextButton(
                    text: "Create Concatenated Field",
                    onClick: {
                        addConcatenatedField()
                    },
                    enabled: !newConcatenationName.isEmpty && !selectedPairsForConcatenation.isEmpty
                )
            }
        }
    }
    
    private var existingMappingsSection: some View {
        Section(header: sectionHeader("Existing Mappings")) {
            if viewModel.state.newBodyFields.isEmpty {
                Text("No field mappings yet")
                    .font(.caption)
                    .foregroundColor(.gray)
            } else {
                ForEach(Array(viewModel.state.newBodyFields.keys), id: \.self) { key in
                    if let oldField = viewModel.state.oldBodyFields[key] {
                        mappingItem(
                            newFieldName: key,
                            sourceFields: oldField.keys,
                            mappingType: determineMappingType(OldBodyField.frommodel(oldField)),
                            onRemove: {
                                removeMapping(fieldName: key)
                            }
                        )
                    }
                }
            }
        }
    }
    
    private func mappingItem(
        newFieldName: String,
        sourceFields: [String],
        mappingType: String,
        onRemove: @escaping () -> Void
    ) -> some View {
        VStack(alignment: .leading) {
            HStack {
                VStack(alignment: .leading) {
                    Text("→ \(newFieldName)")
                        .font(.headline)
                    
                    Text(mappingDescription(mappingType, sourceFields))
                        .font(.subheadline)
                    
                    Text("Type: \(mappingType)")
                        .font(.caption)
                }
                
                Spacer()
                
                Button(action: onRemove) {
                    Image(systemName: "trash")
                        .foregroundColor(.red)
                }
            }
            .padding()
            .background(mappingTypeBackground(mappingType))
            .cornerRadius(8)
        }
    }
    
    // MARK: - Helper Functions
    
    private func sectionHeader(_ title: String) -> some View {
        Text(title)
            .font(.headline)
            .foregroundColor(Color.primaryColor)
            .frame(maxWidth: .infinity, alignment: .leading)
    }
    
    private func addSingleFieldMapping() {
        viewModel.onEvent(event: FillRouteFieldsEvent.UpsertNewBodyField(
            key: newSingleFieldName,
            field: NewBodyField(key: newSingleFieldName, type: inferFieldType(selectedSingleField)).toModel()
        ))
        
        viewModel.onEvent(event: FillRouteFieldsEvent.UpsertOldBodyField(
            key: newSingleFieldName,
            field: OldBodyField(
                keys: [selectedSingleField],
                type: inferFieldType(selectedSingleField),
                parents: []
            ).toModel()
        ))
        
        newSingleFieldName = ""
        selectedSingleField = ""
    }
    
    private func addFieldGroup() {
        viewModel.onEvent(event: FillRouteFieldsEvent.UpsertNewBodyField(
            key: newGroupName,
            field: NewBodyField(key: newGroupName, type: "String").toModel()
        ))
        
        viewModel.onEvent(event: FillRouteFieldsEvent.UpsertOldBodyField(
            key: newGroupName,
            field: OldBodyField(
                keys: Array(selectedFieldsForGroup),
                type: "String",
                parents: []
            ).toModel()
        ))
        
        newGroupName = ""
        selectedFieldsForGroup = []
    }
    
    private func addConcatenatedField() {
        viewModel.onEvent(event: FillRouteFieldsEvent.UpsertNewBodyField(
            key: newConcatenationName,
            field: NewBodyField(key: newConcatenationName, type: "String").toModel()
        ))
        
        viewModel.onEvent(event: FillRouteFieldsEvent.UpsertOldBodyField(
            key: newConcatenationName,
            field: OldBodyField(
                keys: selectedPairsForConcatenation.map { "\($0.first),\($0.second)" },
                type: "String",
                parents: []
            ).toModel()
        ))
        
        newConcatenationName = ""
        selectedPairsForConcatenation = []
    }
    
    private func removeMapping(fieldName: String) {
        viewModel.onEvent(event: FillRouteFieldsEvent.RemoveNewBodyField(key: fieldName))
        viewModel.onEvent(event: FillRouteFieldsEvent.RemoveOldBodyField(key: fieldName))
    }
    
    private func determineMappingType(_ field: OldBodyField) -> String {
        if field.keys.contains(where: { $0.contains(",") }) {
            return "CONCATENATED"
        } else if field.keys.count > 1 {
            return "GROUPED"
        } else {
            return "SINGLE"
        }
    }
    
    private func mappingDescription(_ type: String, _ fields: [String]) -> String {
        switch type {
        case "CONCATENATED":
            let pairs = fields.map { $0.components(separatedBy: ",") }
            return "Concatenates: " + pairs.map { "\($0[0]) + \($0[1])" }.joined(separator: ", ")
        case "GROUPED":
            return "Groups: " + fields.joined(separator: ", ")
        default:
            return "Maps from: \(fields.first ?? "")"
        }
    }
    
    private func mappingTypeBackground(_ type: String) -> Color {
        switch type {
        case "CONCATENATED": return Color.secondary.opacity(0.2)
        case "GROUPED": return Color.tertiary.opacity(0.2)
        default: return Color.surface.opacity(0.5)
        }
    }
    
    private func inferFieldType(_ fieldName: String) -> String {
        if fieldName.lowercased().contains("date") { return "String" }
        if fieldName.lowercased().contains("id") { return "String" }
        if fieldName.lowercased().contains("name") { return "String" }
        if fieldName.lowercased().contains("count") { return "Int" }
        if fieldName.lowercased().contains("is") { return "Boolean" }
        return "String"
    }
    
    private func combineOldBodyFields(
        originalFields: [String: OldBodyField],
        newMappings: [String: OldBodyField]
    ) -> [String: OldBodyField] {
        var result: [String: OldBodyField] = [:]

        for (fieldName, newField) in newMappings {
            let originalField = originalFields.values.first { original in
                original.keys.contains { originalKey in
                    newField.keys.contains { newKey in
                        newKey.contains(originalKey) || originalKey.contains(newKey)
                    }
                }
            } ?? originalFields[fieldName]

            if let original = originalField {
                let updated = OldBodyField(
                    keys: newField.keys,
                    type: original.type,
                    parents: original.parents
                )
                result[fieldName] = updated
            } else {
                result[fieldName] = newField
            }
        }

        return result
    }
    
    private func setupScreen() {
        viewModel.startObserving()
        subscribeToEvents()
    }
    
    private func cleanup() {
        viewModel.dispose()
        eventSubscription?.dispose()
    }
    
    private func subscribeToEvents() {
        eventSubscription = viewModel.eventFlow.subscribe(
            onCollect: { event in
                DispatchQueue.main.async {
                    guard let event = event else { return }
                    if let errorEvent = event as? FillRouteFieldsViewModel.UiEventShowError {
                        errorMessage = errorEvent.message
                        showErrorAlert = true
                    }
                }
            }
        )
    }
}

// MARK: - Supporting Views

struct FieldSelectorDropdown: View {
    let fields: [String]
    @Binding var selectedField: String
    let label: String
    
    @State private var showDropdown = false
    
    var body: some View {
        VStack {
            Button(action: { showDropdown = true }) {
                HStack {
                    Text(selectedField.isEmpty ? label : selectedField)
                        .foregroundColor(selectedField.isEmpty ? .gray : .primary)
                    Spacer()
                    Image(systemName: "chevron.down")
                }
                .padding()
                .background(Color.surface)
                .cornerRadius(8)
            }
            .actionSheet(isPresented: $showDropdown) {
                ActionSheet(
                    title: Text(label),
                    buttons: fields.map { field in
                        .default(Text(field)) {
                            selectedField = field
                        }
                    } + [.cancel()]
                )
            }
        }
    }
}

struct CheckboxToggleStyle: ToggleStyle {
    func makeBody(configuration: Configuration) -> some View {
        HStack {
            Image(systemName: configuration.isOn ? "checkmark.square" : "square")
                .resizable()
                .frame(width: 20, height: 20)
                .foregroundColor(configuration.isOn ? .primaryColor : .white)
            configuration.label
        }
        .contentShape(Rectangle()) // Make the entire row tappable
        .onTapGesture {
            configuration.isOn.toggle()
        }
    }
}
