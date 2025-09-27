package net.botwithus.kxapi.script.snapshot

import kotlinx.coroutines.CancellableContinuation

/**
 * Internal data class representing a pending wait request for coroutine suspension.
 * 
 * @param continuation The coroutine continuation to resume when the wait is complete
 * @param resumeTick The server tick when this wait request should be resumed
 */
internal data class WaitRequest(
    val continuation: CancellableContinuation<Unit>,
    val resumeTick: Int
)

