package io.lb.middleware.common.state

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

expect class CommonMutableStateFlow<T>(flow: MutableStateFlow<T>): MutableStateFlow<T> {
    override var value: T
    override val subscriptionCount: StateFlow<Int>
    override suspend fun collect(collector: FlowCollector<T>): Nothing
    override fun compareAndSet(expect: T, update: T): Boolean
    override val replayCache: List<T>
    override suspend fun emit(value: T)
    override fun tryEmit(value: T): Boolean
    override fun resetReplayCache()
}

fun <T> MutableStateFlow<T>.toCommonMutableStateFlow(): CommonMutableStateFlow<T> =
    CommonMutableStateFlow(this)
