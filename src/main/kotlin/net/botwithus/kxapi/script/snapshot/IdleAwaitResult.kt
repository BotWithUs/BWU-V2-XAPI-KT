package net.botwithus.kxapi.script.snapshot

/**
 * Result of awaiting player idleness. Provides success and timeout cases with diagnostic details.
 */
sealed class IdleAwaitResult {
    
    /**
     * Player became idle successfully
     * @param snapshot The player activity snapshot when idle was detected
     */
    data class Idle(val snapshot: PlayerActivitySnapshot) : IdleAwaitResult()
    
    /**
     * Timeout occurred while waiting for player to become idle
     * @param snapshot The player activity snapshot at timeout
     * @param animationActive Whether animation was still active at timeout
     * @param movementActive Whether movement was still active at timeout
     */
    data class Timeout(
        val snapshot: PlayerActivitySnapshot,
        val animationActive: Boolean,
        val movementActive: Boolean
    ) : IdleAwaitResult()
}

