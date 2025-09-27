package net.botwithus.kxapi.script.iterator

import net.botwithus.kxapi.permissive.BaseStateEnum

/**
 * A utility class for managing sequential state iteration with skip conditions.
 * 
 * This class provides a way to iterate through states in a predefined order,
 * with the ability to skip certain states based on conditions.
 * 
 * @param T The state enum type that implements BaseStateEnum
 * 
 * @author Generated
 * @since 1.0.0
 */
class StateIterator<T>(
    private val states: List<T>,
    private val skipConditions: Map<T, () -> Boolean> = emptyMap() // Map of state -> condition
) where T : Enum<T>, T : BaseStateEnum {
    
    private var index = 0
    private var lastState: T? = null
    private var currentState: T? = states.getOrNull(0)

    /**
     * Move to the next valid state (skipping states that meet their skip conditions)
     * @return The next valid state
     */
    fun next(): T {
        lastState = currentState
        val nextValidState = states.drop(index + 1).firstOrNull { !shouldSkip(it) } // Find next state that isn't skipped

        if (nextValidState != null) {
            index = states.indexOf(nextValidState)
            currentState = nextValidState
        }

        logStateChange()
        return currentState!!
    }

    /**
     * Reset the iterator to the first state
     */
    fun reset() {
        index = 0
        lastState = null
        currentState = states.getOrNull(0)
    }

    /**
     * Check if a state exists in the iterator
     * @param state The state to check
     * @return True if the state exists
     */
    fun hasState(state: T) = state in states

    /**
     * Get the current state
     * @return The current state or null if none
     */
    fun getCurrentState(): T? = currentState

    /**
     * Get the last state
     * @return The last state or null if none
     */
    fun getLastState(): T? = lastState

    /**
     * Set the current state to a specific state
     * @param state The state to set as current
     */
    fun setState(state: T) {
        val newIndex = states.indexOf(state)
        if (newIndex != -1) {
            lastState = currentState
            currentState = state
            index = newIndex
            logStateChange()
        } else {
            println("State '$state' does not exist in the available states.")
        }
    }

    /**
     * Check if a state should be skipped based on its condition
     * @param state The state to check
     * @return True if the state should be skipped
     */
    private fun shouldSkip(state: T): Boolean {
        return skipConditions[state]?.invoke() ?: false // Call condition function if it exists
    }

    /**
     * Log state changes for debugging
     */
    private fun logStateChange() {
        println("State changed from: $lastState to $currentState")
    }

    /**
     * Get all states in the iterator
     * @return List of all states
     */
    fun getAllStates(): List<T> = states.toList()

    /**
     * Get the current index
     * @return Current index in the states list
     */
    fun getCurrentIndex(): Int = index

    /**
     * Check if there are more states after the current one
     * @return True if there are more states
     */
    fun hasNext(): Boolean {
        return states.drop(index + 1).any { !shouldSkip(it) }
    }

    /**
     * Get the next state without advancing the iterator
     * @return The next state or null if none
     */
    fun peekNext(): T? {
        return states.drop(index + 1).firstOrNull { !shouldSkip(it) }
    }
}

