package io.lb.middleware.common.state

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

actual class CommonFlow<T> actual constructor(
    private val flow: Flow<T>
) : Flow<T> by flow
