package io.lb.middleware.common.shared.util

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

fun beautifyJson(jsonString: String): String {
    val json = Json {
        prettyPrint = true
    }
    val jsonObject = json.parseToJsonElement(jsonString).jsonObject
    return json.encodeToString(JsonObject.serializer(), jsonObject)
}
