package net.botwithus.kxapi.script.snapshot

/**
 * Snapshot capturing minimal player activity information for idle checks.
 * 
 * @param captureTick The server tick when this snapshot was captured
 * @param animationId The player's current animation ID (-1 if idle)
 * @param isMoving Whether the player is currently moving (null if unknown)
 */
data class PlayerActivitySnapshot(
    val captureTick: Int,
    val animationId: Int,
    val isMoving: Boolean?
) {
    /**
     * Returns true if the player's animation is idle (animationId == -1)
     */
    val isAnimationIdle: Boolean get() = animationId == -1
    
    /**
     * Returns true if the player is not moving
     */
    val isMovementIdle: Boolean get() = isMoving != true
}

