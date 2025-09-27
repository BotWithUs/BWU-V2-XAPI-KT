package net.botwithus.kxapi.script

import net.botwithus.kxapi.permissive.BaseStateEnum
import net.botwithus.kxapi.script.iterator.StateIterator
import java.lang.reflect.ParameterizedType

/**
 * Base class for state-based scripts that provides common state management functionality.
 *
 * This class contains the shared logic for state iteration, skip conditions, and state management
 * that is common between StateScript and PermissiveScript.
 *
 * @param State The enum class that implements BaseStateEnum interface
 * @author Generated
 * @since 1.0.0
 */
abstract class BaseStateScript<State>(
    protected val debug: Boolean = false,
    private val states: List<State> = emptyList()
) : SuspendableScript()
    where State : Enum<State>, State : BaseStateEnum {

    init {
        isDebugMode = debug
    }

    protected var _stateIterator: StateIterator<State>? = null
    protected var _skipConditions: Map<State, () -> Boolean> = emptyMap()

    protected val actualStates: List<State> by lazy {
        states.ifEmpty {
            val enumClass = getEnumClass()
            if (enumClass != null) {
                enumClass.enumConstants?.toList() ?: emptyList()
            } else {
                emptyList()
            }
        }
    }

    /**
     * Get the enum class from the generic type parameter.
     */
    private fun getEnumClass(): Class<State>? {
        return try {
            val genericSuperclass = this::class.java.genericSuperclass
            if (genericSuperclass is ParameterizedType) {
                val actualTypeArguments = genericSuperclass.actualTypeArguments
                if (actualTypeArguments.isNotEmpty()) {
                    val firstType = actualTypeArguments[0]
                    if (firstType is Class<*>) {
                        firstType as? Class<State>
                    } else null
                } else null
            } else null
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Override this method to set skip conditions for states
     * @return Map of state to skip condition function
     */
    protected open fun getSkipConditions(): Map<State, () -> Boolean> = emptyMap()

    /**
     * Update skip conditions and recreate the iterator
     * @param newSkipConditions New skip conditions to apply
     */
    protected fun updateSkipConditions(newSkipConditions: Map<State, () -> Boolean>) {
        _skipConditions = newSkipConditions
        _stateIterator = StateIterator(actualStates, _skipConditions)
    }

    /**
     * Create the state iterator with the actual states and skip conditions
     * @return The created StateIterator
     */
    protected fun createStateIterator(): StateIterator<State> {
        _stateIterator = StateIterator(actualStates, _skipConditions)
        return _stateIterator!!
    }

    /**
     * Get the current state iterator
     * @return The current StateIterator or null if not created
     */
    protected fun getStateIterator(): StateIterator<State>? = _stateIterator

    /**
     * Move to the next state using the iterator
     * @return The next state or null if no iterator is set
     */
    protected open fun nextState(): State? {
        return _stateIterator?.next()
    }

    /**
     * Reset the state iterator
     */
    protected open fun resetStateIterator() {
        _stateIterator?.reset()
    }

    /**
     * Set the current state using the iterator
     * @param state The state to set
     */
    open fun setState(state: State) {
        _stateIterator?.setState(state)
    }

    override fun onInitialize() {
        super.onInitialize()
        // Initialize skip conditions from implementation
        _skipConditions = getSkipConditions()

        // Call the abstract initialization method
        initializeStates()

        // Create the state iterator
        createStateIterator()
    }

    /**
     * Abstract method to initialize states - implemented by subclasses
     */
    protected abstract fun initializeStates()
}