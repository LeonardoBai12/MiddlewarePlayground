package io.lb.middleware.android.createroute.presentation.model

import android.os.Parcelable
import io.lb.middleware.common.shared.middleware.model.OldBodyField
import kotlinx.parcelize.Parcelize

@Parcelize
data class AndroidOldBodyField(
    val keys: List<String>,
    val type: String,
    val parents: List<String> = emptyList()
) : Parcelable {
    companion object {
        fun fromOldBodyField(oldBodyField: OldBodyField): AndroidOldBodyField {
            return AndroidOldBodyField(
                keys = oldBodyField.keys,
                type = oldBodyField.type,
                parents = oldBodyField.parents
            )
        }
    }
}
