package io.lb.middleware.android.core.util

import io.lb.middleware.common.shared.middleware.model.OldBodyField
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.intOrNull

fun generateOldBodyFieldsFromJson(jsonString: String): Map<String, OldBodyField> {
    val json = Json.parseToJsonElement(jsonString)
    val fields = mutableMapOf<String, OldBodyField>()
    processJsonElement(json, fields, emptyList())
    return fields
}

private fun processJsonElement(
    element: JsonElement,
    fields: MutableMap<String, OldBodyField>,
    parents: List<String>
) {
    when (element) {
        is JsonObject -> {
            element.forEach { (key, value) ->
                when (value) {
                    is JsonObject -> {
                        processJsonElement(value, fields, parents + key)
                    }
                    is JsonPrimitive -> {
                        val fieldName = if (parents.isEmpty()) key else "${parents.joinToString("_")}_$key"
                        val type = when {
                            value.isString -> "String"
                            value.intOrNull != null -> "Int"
                            value.doubleOrNull != null -> "Double"
                            value.booleanOrNull != null -> "Boolean"
                            else -> "String" // fallback
                        }
                        fields[fieldName] = OldBodyField(
                            keys = listOf(key),
                            type = type,
                            parents = parents.toList()
                        )
                    }
                    is JsonArray -> {
                        if (value.isNotEmpty() && value[0] is JsonObject) {
                            processJsonElement(value[0], fields, parents + key)
                        }
                    }
                }
            }
        }
        is JsonArray -> {
            if (element.isNotEmpty() && element[0] is JsonObject) {
                processJsonElement(element[0], fields, parents)
            }
        }
        else -> {
            // ignore
        }
    }
}
