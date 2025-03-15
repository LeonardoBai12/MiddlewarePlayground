package io.lb.middleware.impl.client.middleware.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a field in the old body.
 *
 * @property keys The keys of the field. There should be at least one key.
 * @property type The type of the field. It could be one on these Kotlin primitive types.
 * <br>Allowed types:
 * <br>- [String]
 * <br>- [Int]
 * <br>- [Double]
 * <br>- [Boolean]
 * @property parents The parent keys of the field. It will be empty if it's a root field.
 */
@Serializable
data class OldBodyFieldParameter(
    @SerialName("keys")
    val keys: List<String>,
    @SerialName("type")
    val type: String,
    @SerialName("parents")
    val parents: List<String> = emptyList()
)
