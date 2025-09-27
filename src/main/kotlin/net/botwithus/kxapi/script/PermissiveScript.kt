package net.botwithus.kxapi.script

import net.botwithus.kxapi.permissive.PermissiveDSL
import net.botwithus.kxapi.permissive.PermissiveStateEnum

/**
 * Enhanced PermissiveScript that supports enum-based state management with embedded class references.
 *
 * This class works with enums that implement the PermissiveStateEnum interface, ensuring they contain
 * both a description and a class reference that extends PermissiveDSL.
 *
 * @param State The enum class that implements PermissiveStateEnum interface
 *
 */
abstract class PermissiveScript<State>(debug: Boolean) : BaseStateScript<State>(debug) where State : Enum<State>, State : PermissiveStateEnum {

    private val stateInstances = mutableMapOf<State, PermissiveDSL<*>>()

    /**
     * Get or create an instance of the state for the given enum
     * @param stateEnum The enum value representing the state
     * @return The state instance
     */
    protected fun getState(stateEnum: State): PermissiveDSL<*>? {
        return stateInstances.getOrPut(stateEnum) {
            val stateClass = stateEnum.classz

            stateClass.java.getDeclaredConstructor(this::class.java, String::class.java)
                .newInstance(this, stateEnum.description)
                ?: throw IllegalStateException("Could not create instance of $stateClass. Make sure the constructor takes (script: BwuScript, name: String)")
        }
    }

    abstract fun init()

    override suspend fun onLoop() {
        this.doRun()
    }

    /**
     * Get a state instance by enum value
     * @param stateEnum The state enum to get
     * @return The state instance or null if not found
     */
    protected fun getStateInstance(stateEnum: State): PermissiveDSL<*>? {
        return getState(stateEnum)
    }

    /**
     * Check if a state is currently active
     * @param stateEnum The state to check
     * @return True if the state is currently active
     */
    protected fun isStateActive(stateEnum: State): Boolean {
        return currentState.name == stateEnum.description
    }

    /**
     * Move to the next state using the iterator and update BwuScript currentState
     * @return The next state or null if no iterator is set
     */
    override fun nextState(): State? {
        val next = super.nextState()
        next?.let { state ->
            setCurrentState(state.description)
            status = "Switched to ${state.description}"
        }
        return next
    }

    /**
     * Set the current state in the iterator and update BwuScript currentState
     * @param state The state to set
     */
    override fun setState(state: State) {
        super.setState(state)
        setCurrentState(state.description)
        status = "Switched to ${state.description}"
    }


    override fun initializeStates() {
        init()

        val stateInstances = actualStates.mapNotNull { getState(it) }.toTypedArray()

        // Call initStates with all state instances
        initStates(*stateInstances)

        status = "Script initialized with state: ${currentState.name}"
    }
}