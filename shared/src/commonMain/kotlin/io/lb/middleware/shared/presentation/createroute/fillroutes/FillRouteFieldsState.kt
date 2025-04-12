package io.lb.middleware.shared.presentation.createroute.fillroutes

import io.lb.middleware.common.shared.middleware.model.NewBodyField
import io.lb.middleware.common.shared.middleware.model.OldBodyField

data class FillRouteFieldsState(
    val newBodyFields: Map<String, NewBodyField> = emptyMap(),
    val oldBodyFields: Map<String, OldBodyField> = emptyMap(),
)
