package io.lb.middleware.common.shared.middleware.model

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
data class OldBodyField(
    val keys: List<String>,
    val type: String,
    val parents: List<String> = emptyList()
)
