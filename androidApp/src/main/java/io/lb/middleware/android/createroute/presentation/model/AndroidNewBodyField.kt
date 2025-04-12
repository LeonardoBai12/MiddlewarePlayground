package io.lb.middleware.android.createroute.presentation.model

import android.os.Parcelable
import io.lb.middleware.common.shared.middleware.model.NewBodyField
import kotlinx.parcelize.Parcelize

@Parcelize
data class AndroidNewBodyField(
    val key: String,
    val type: String
) : Parcelable {
    fun toNewBodyField(): NewBodyField {
        return NewBodyField(
            key = key,
            type = type
        )
    }

    companion object {
        fun fromNewBodyField(newBodyField: NewBodyField): AndroidNewBodyField {
            return AndroidNewBodyField(
                key = newBodyField.key,
                type = newBodyField.type
            )
        }
    }
}
