package io.lb.middleware.impl.client.middleware.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a new body mapping rule.
 *
 * - Json example of the rules:
 * [RulesExample.json](https://github.com/LeonardoBai12-Org/ProjectMiddleware/blob/main/JsonExamples/RulesExample.json)
 * [ConcatenatedRulesExample.json](https://github.com/LeonardoBai12-Org/ProjectMiddleware/blob/main/JsonExamples/ConcatenatedRulesExample.json)
 *
 * - Json example of a response provided by [TheMealDB](https://www.themealdb.com/api.php).
 * [ExampleBefore.json](https://github.com/LeonardoBai12-Org/ProjectMiddleware/blob/main/JsonExamples/ExampleBefore.json)
 *
 * - Json example of a mapped response using the rules from the example above:
 * [ExampleAfter.json](https://github.com/LeonardoBai12-Org/ProjectMiddleware/blob/main/JsonExamples/ExampleAfter.json)
 *
 * - Json example of a mapped response using the rules from the example above (with empty values):
 * [ExampleAfter.json](https://github.com/LeonardoBai12-Org/ProjectMiddleware/blob/main/JsonExamples/ExampleAfterWithEmptyValues.json)
 *
 * - Json example of a mapped response using concatenation of values using the rules
 * from the example above (with empty values):
 * [ConcatenatedAfter.json](https://github.com/LeonardoBai12-Org/ProjectMiddleware/blob/main/JsonExamples/ConcatenatedAfter.json)
 *
 * @property newBodyFields The new body fields.
 * @property oldBodyFields The old body fields.
 * @property ignoreEmptyValues Whether to ignore empty values.
 */
@Serializable
data class NewBodyMappingRuleParameter(
    @SerialName("newBodyFields")
    val newBodyFields: Map<String, NewBodyFieldParameter>,
    @SerialName("oldBodyFields")
    val oldBodyFields: Map<String, OldBodyFieldParameter>,
    @SerialName("ignoreEmptyValues")
    val ignoreEmptyValues: Boolean = false
)
