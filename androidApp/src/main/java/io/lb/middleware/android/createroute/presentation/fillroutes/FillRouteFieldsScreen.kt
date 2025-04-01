package io.lb.middleware.android.createroute.presentation.fillroutes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.lb.middleware.android.core.presentation.Screens
import io.lb.middleware.android.core.presentation.components.DefaultCard
import io.lb.middleware.android.core.presentation.components.DefaultIconButton
import io.lb.middleware.android.core.presentation.components.DefaultTextButton
import io.lb.middleware.android.core.presentation.components.DefaultTextField
import io.lb.middleware.android.core.presentation.components.GenericTopAppBar
import io.lb.middleware.android.core.presentation.showToast
import io.lb.middleware.android.createroute.presentation.fillpreconfigs.FillPreConfigsArgs
import io.lb.middleware.android.createroute.presentation.model.AndroidNewBodyField
import io.lb.middleware.android.createroute.presentation.model.AndroidOldBodyField
import io.lb.middleware.common.shared.middleware.model.NewBodyField
import io.lb.middleware.common.shared.middleware.model.OldBodyField
import io.lb.middleware.common.shared.middleware.request.MiddlewareHttpMethods
import io.lb.middleware.common.state.CommonFlow
import io.lb.middleware.shared.presentation.createroute.fillroutes.FillRouteFieldsEvent
import io.lb.middleware.shared.presentation.createroute.fillroutes.FillRouteFieldsState
import io.lb.middleware.shared.presentation.createroute.fillroutes.FillRouteFieldsViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillRouteFieldsScreen(
    navController: NavHostController,
    args: FillRoutesFieldsArgs?,
    state: FillRouteFieldsState,
    eventFlow: CommonFlow<FillRouteFieldsViewModel.UiEvent>,
    onEvent: (FillRouteFieldsEvent) -> Unit
) {
    val context = LocalContext.current
    val oldBodyFieldsKeys = args?.oldBodyFields?.values?.flatMap { it.keys }?.distinct() ?: emptyList()

    val newSingleFieldName = remember { mutableStateOf("") }
    val selectedSingleField = remember { mutableStateOf("") }

    val newGroupName = remember { mutableStateOf("") }
    val selectedFieldsForGroup = remember { mutableStateListOf<String>() }

    val newConcatenationName = remember { mutableStateOf("") }
    val selectedPairsForConcatenation = remember { mutableStateListOf<Pair<String, String>>() }
    val firstConcatField = remember { mutableStateOf("") }
    val secondConcatField = remember { mutableStateOf("") }

    LaunchedEffect(key1 = Screens.FILL_ROUTE_FIELDS) {
        eventFlow.collectLatest {
            when (it) {
                is FillRouteFieldsViewModel.UiEvent.ShowError -> {
                    context.showToast(it.message)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            GenericTopAppBar(navController, "Step 2: Map Response Fields")
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            DefaultTextButton(
                modifier = Modifier.fillMaxWidth()
                    .padding(
                        bottom = 8.dp,
                        start = 32.dp,
                        end = 32.dp
                    ),
                text = "Move Forward",
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onClick = {
                    if (state.newBodyFields.isEmpty()) {
                        context.showToast("Please add at least one mapping")
                        return@DefaultTextButton
                    }
                    val oldBodyFields = combineOldBodyFields(
                        originalFields = args?.oldBodyFields ?: emptyMap(),
                        newMappings = state.oldBodyFields
                    )
                    val newArgs = FillPreConfigsArgs(
                        originalBaseUrl = args?.originalBaseUrl ?: "",
                        originalPath = args?.originalPath ?: "",
                        originalMethod = args?.originalMethod ?: MiddlewareHttpMethods.Get,
                        originalBody = args?.originalBody ?: "",
                        originalQueries = args?.originalQueries ?: emptyMap(),
                        oldBodyFields = oldBodyFields,
                        newBodyField = state.newBodyFields.mapValues { (_, newBodyField) ->
                            AndroidNewBodyField.fromNewBodyField(newBodyField)
                        }
                    )
                    navController.currentBackStackEntry?.arguments?.putParcelable("FillPreConfigsArgs", newArgs)
                    navController.navigate(Screens.FILL_PRE_CONFIGS.name)
                }
            )

            // 1. Single Field Mapping
            SingleFieldMappingSection(
                newFieldName = newSingleFieldName,
                selectedField = selectedSingleField,
                availableFields = oldBodyFieldsKeys.filter { field ->
                    !state.oldBodyFields.values.any { it.keys.contains(field) }
                },
                onAddMapping = { newName, field ->
                    // New body just stores the new name
                    onEvent(FillRouteFieldsEvent.UpsertNewBodyField(
                        newName,
                        NewBodyField(key = newName, type = inferFieldType(field))
                    ))
                    // Old body stores the source field
                    onEvent(FillRouteFieldsEvent.UpsertOldBodyField(
                        newName,
                        OldBodyField(keys = listOf(field), type = inferFieldType(field), parents = emptyList())
                    ))
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Field Grouping
            FieldGroupingSection(
                newGroupName = newGroupName,
                selectedFields = selectedFieldsForGroup,
                availableFields = oldBodyFieldsKeys.filter { field ->
                    !state.oldBodyFields.values.any { it.keys.contains(field) }
                },
                onAddGroup = { newName, fields ->
                    // New body just stores the new name
                    onEvent(FillRouteFieldsEvent.UpsertNewBodyField(
                        newName,
                        NewBodyField(key = newName, type = "String")
                    ))
                    // Old body stores all source fields
                    onEvent(FillRouteFieldsEvent.UpsertOldBodyField(
                        newName,
                        OldBodyField(keys = fields, type = "String", parents = emptyList())
                    ))
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Field Concatenation
            FieldConcatenationSection(
                newConcatenationName = newConcatenationName,
                firstField = firstConcatField,
                secondField = secondConcatField,
                selectedPairs = selectedPairsForConcatenation,
                availableFields = oldBodyFieldsKeys.filter { field ->
                    !state.oldBodyFields.values.any { it.keys.any { k -> k.contains(field) } }
                },
                onAddConcatenation = { newName, pairs ->
                    // New body just stores the new name
                    onEvent(FillRouteFieldsEvent.UpsertNewBodyField(
                        newName,
                        NewBodyField(key = newName, type = "String")
                    ))
                    // Old body stores concatenation rules as "field1,field2" pairs
                    onEvent(FillRouteFieldsEvent.UpsertOldBodyField(
                        newName,
                        OldBodyField(
                            keys = pairs.map { (first, second) -> "$first,$second" },
                            type = "String",
                            parents = emptyList()
                        )
                    ))
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Existing Mappings
            ExistingMappingsSection(
                state = state,
                onRemoveMapping = { fieldName ->
                    onEvent(FillRouteFieldsEvent.RemoveNewBodyField(fieldName))
                    onEvent(FillRouteFieldsEvent.RemoveOldBodyField(fieldName))
                }
            )
        }
    }
}

@Composable
private fun ExistingMappingsSection(
    state: FillRouteFieldsState,
    onRemoveMapping: (String) -> Unit
) {
    Column {
        Text(
            text = "Existing Mappings",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (state.newBodyFields.isEmpty()) {
            Text(
                text = "No field mappings yet",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            state.newBodyFields.forEach { (newKey, newBodyField) ->
                val oldField = state.oldBodyFields[newKey]
                val mappingType = when {
                    oldField?.keys?.size ?: 0 > 1 -> {
                        if (newBodyField.key.contains('|')) "CONCATENATED" else "GROUPED"
                    }
                    else -> "SINGLE"
                }

                MappingItem(
                    newFieldName = newKey,
                    sourceFields = oldField?.keys ?: emptyList(),
                    mappingType = mappingType,
                    onRemove = { onRemoveMapping(newKey) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun MappingItem(
    newFieldName: String,
    sourceFields: List<String>,
    mappingType: String,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (mappingType) {
                "CONCATENATED" -> MaterialTheme.colorScheme.secondaryContainer
                "GROUPED" -> MaterialTheme.colorScheme.tertiaryContainer
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "→ $newFieldName",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = when (mappingType) {
                            "CONCATENATED" -> "Concatenates: ${sourceFields.chunked(2).joinToString { it.joinToString(" + ") }}"
                            "GROUPED" -> "Groups: ${sourceFields.joinToString(", ")}"
                            else -> "Maps from: ${sourceFields.firstOrNull() ?: "Unknown"}"
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Type: $mappingType",
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                DefaultIconButton(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove mapping",
                    onClick = onRemove
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun SingleFieldMappingSection(
    newFieldName: MutableState<String>,
    selectedField: MutableState<String>,
    availableFields: List<String>,
    onAddMapping: (String, String) -> Unit
) {
    Column {
        Text(
            text = "Single Field Mapping",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        DefaultTextField(
            modifier = Modifier.fillMaxWidth(),
            text = newFieldName.value,
            label = "New Field Name",
            onValueChange = { newFieldName.value = it }
        )

        Spacer(modifier = Modifier.height(12.dp))

        FieldSelectorDropdown(
            fields = availableFields,
            selectedField = selectedField.value,
            label = "Select source field",
            onFieldSelected = { selectedField.value = it }
        )

        Spacer(modifier = Modifier.height(12.dp))

        DefaultTextButton(
            modifier = Modifier.align(Alignment.End),
            text = "Add Single Field",
            enabled = newFieldName.value.isNotBlank() && selectedField.value.isNotBlank(),
            onClick = {
                onAddMapping(newFieldName.value, selectedField.value)
                newFieldName.value = ""
                selectedField.value = ""
            }
        )
    }
}

@ExperimentalMaterial3Api
@Composable
private fun FieldConcatenationSection(
    newConcatenationName: MutableState<String>,
    firstField: MutableState<String>,
    secondField: MutableState<String>,
    selectedPairs: MutableList<Pair<String, String>>,
    availableFields: List<String>,
    onAddConcatenation: (String, List<Pair<String, String>>) -> Unit
) {
    Column {
        Text(
            text = "Field Concatenation",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        DefaultTextField(
            modifier = Modifier.fillMaxWidth(),
            text = newConcatenationName.value,
            label = "New Concatenation Name",
            onValueChange = { newConcatenationName.value = it }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Select field pairs to concatenate:",
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // First field dropdown
            FieldSelectorDropdown(
                modifier = Modifier.weight(1f),
                fields = availableFields,
                selectedField = firstField.value,
                label = "First field",
                onFieldSelected = { firstField.value = it }
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Second field dropdown
            FieldSelectorDropdown(
                modifier = Modifier.weight(1f),
                fields = availableFields.filter { it != firstField.value },
                selectedField = secondField.value,
                label = "Second field",
                onFieldSelected = { secondField.value = it }
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Add pair button
        DefaultTextButton(
            modifier = Modifier.align(Alignment.End),
            text = "Add Pair",
            enabled = firstField.value.isNotBlank() && secondField.value.isNotBlank(),
            onClick = {
                selectedPairs.add(firstField.value to secondField.value)
                firstField.value = ""
                secondField.value = ""
            }
        )

        // Show selected pairs
        selectedPairs.forEach { (first, second) ->
            Text(
                text = "$first + $second",
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Create concatenation button
        DefaultTextButton(
            modifier = Modifier.align(Alignment.End),
            text = "Create Concatenated Field",
            enabled = newConcatenationName.value.isNotBlank() && selectedPairs.isNotEmpty(),
            onClick = {
                onAddConcatenation(newConcatenationName.value, selectedPairs.toList())
                newConcatenationName.value = ""
                selectedPairs.clear()
            }
        )
    }
}

@ExperimentalMaterial3Api
@Composable
private fun FieldSelectorDropdown(
    modifier: Modifier = Modifier,
    fields: List<String>,
    selectedField: String,
    label: String,
    onFieldSelected: (String) -> Unit
) {
    var expanded = remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        DefaultCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = { expanded.value = true }
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedField.ifBlank { label },
                    color = if (selectedField.isBlank()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded.value) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = if (expanded.value) "Collapse" else "Expand"
                )
            }
        }

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            if (fields.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No available fields") },
                    onClick = { expanded.value = false }
                )
            } else {
                fields.forEach { field ->
                    DropdownMenuItem(
                        text = { Text(text = field) },
                        onClick = {
                            onFieldSelected(field)
                            expanded.value = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FieldGroupingSection(
    newGroupName: MutableState<String>,
    selectedFields: MutableList<String>,
    availableFields: List<String>,
    onAddGroup: (String, List<String>) -> Unit
) {
    Column {
        Text(
            text = "Field Grouping (Multiple Fields → Array)",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        DefaultTextField(
            modifier = Modifier.fillMaxWidth(),
            text = newGroupName.value,
            label = "New Group Name",
            onValueChange = { newGroupName.value = it }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Select fields to group:",
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (availableFields.isEmpty()) {
            Text(
                text = "No fields available for grouping",
                style = MaterialTheme.typography.bodySmall
            )
        } else {
            Column {
                availableFields.forEach { field ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (selectedFields.contains(field)) {
                                    selectedFields.remove(field)
                                } else {
                                    selectedFields.add(field)
                                }
                            }
                    ) {
                        Checkbox(
                            checked = selectedFields.contains(field),
                            onCheckedChange = { checked ->
                                if (checked) {
                                    selectedFields.add(field)
                                } else {
                                    selectedFields.remove(field)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = field)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        DefaultTextButton(
            modifier = Modifier.align(Alignment.End),
            text = "Add Field Group",
            enabled = newGroupName.value.isNotBlank() && selectedFields.isNotEmpty(),
            onClick = {
                onAddGroup(newGroupName.value, selectedFields.toList())
                newGroupName.value = ""
                selectedFields.clear()
            }
        )
    }
}

// Keep all other sections (SingleFieldMappingSection, FieldGroupingSection, ExistingMappingsSection)
// with the same implementation as before, just updating the type signatures to match the new approach

private fun inferFieldType(fieldName: String): String {
    return when {
        fieldName.startsWith("str") -> "String"
        fieldName.startsWith("int") -> "Int"
        fieldName.startsWith("bool") -> "Boolean"
        fieldName.startsWith("dbl") -> "Double"
        else -> "String"
    }
}

fun combineOldBodyFields(
    originalFields: Map<String, AndroidOldBodyField>,
    newMappings: Map<String, OldBodyField>
): Map<String, AndroidOldBodyField> {
    return newMappings.mapValues { (fieldName, newField) ->
        val originalField = originalFields.values.firstOrNull { original ->
            original.keys.any { originalKey ->
                newField.keys.any { newKey ->
                    newKey.contains(originalKey) || originalKey.contains(newKey)
                }
            }
        } ?: originalFields[fieldName]

        originalField?.copy(keys = newField.keys)
            ?: AndroidOldBodyField(
                keys = newField.keys,
                type = newField.type,
                parents = newField.parents
            )
    }
}
