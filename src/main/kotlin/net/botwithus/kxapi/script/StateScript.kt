package net.botwithus.kxapi.script

import net.botwithus.kxapi.permissive.StateEnum
import net.botwithus.kxapi.script.BaseStateScript

/**
 * A script type that extends SuspendableScript with automatic state machine functionality.
 * This provides enum-based state management with automatic state switching and lifecycle management.
 * 
 * @param State The enum class that implements StateEnum interface
 *
 */
abstract class StateScript<State>(
    debug: Boolean,
    states: List<State> = emptyList()
) : BaseStateScript<State>(debug, states)
    where State : Enum<State>, State : StateEnum {

    private var stateMachineReady = false


    /**
     * Override to customize the initial state.
     * Defaults to the enum's first constant.
     */
    protected open val defaultState: State?
        get() = null


    /**
     * Switch to a specific state by enum value
     * @param stateEnum The state to switch to
     */
    protected fun switchToState(stateEnum: State) {
        if (!stateMachineReady) {
            return
        }
        setCurrentState(stateEnum.description)
        status = "Switched to ${stateEnum.description}"
        logger.debug("${javaClass.simpleName} transitioned to state ${stateEnum.description}")
    }

    /**
     * Check if a state is currently active
     * @param stateEnum The state to check
     * @return True if the state is currently active
     */
    protected fun isStateActive(stateEnum: State): Boolean {
        if (!stateMachineReady) return false
        return currentState.name == stateEnum.description
    }


    /**
     * Override this method to implement your script logic.
     * This method is called once per tick and can use suspend functions.
     */
    protected abstract suspend fun onTick()

    /**
     * Default implementation handles state iteration and calls onTick().
     * Override onLoop() if you need different behavior.
     */
    override suspend fun onLoop() {
        onTick()
    }

    override fun initializeStates() {
        stateMachineReady = true

        val initial = defaultState ?: actualStates.firstOrNull()
        if (initial != null) {
            switchToState(initial)
        }
    }


}