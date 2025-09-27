package net.botwithus.kxapi.script

import net.botwithus.kxapi.script.SuspendableScript

/**
 * A simple script type that extends SuspendableScript with basic loop-based execution.
 * This provides the simplest script interface without state management.
 *
 */
abstract class LoopingScript(debug : Boolean = false) : SuspendableScript() {

    init {
        this.isDebugMode = debug
    }

    /**
     * Override this method to implement your script logic.
     * This method is called once per tick and can use suspend functions.
     */
    protected abstract suspend fun onTick()

    /**
     * Default implementation calls onTick().
     * Override onLoop() if you need different behavior.
     */
    override suspend fun onLoop() {
        onTick()
    }

    /**
     * Override this method to perform any initialization when the script starts.
     * This is called during script initialization.
     */
    protected open fun onInit() {}

    override fun onInitialize() {
        super.onInitialize()
        runCatching {
            onInit()
            status = "Looping script initialized"
        }.onFailure { e ->
            logger.error("Failed to initialize looping script: ${e.message}", e)
        }
    }
}