package net.botwithus.kxapi.permissive

import kotlin.reflect.KClass

/**
 * Interface for permissive states that require class information for instantiation.
 * This is used by PermissiveScript for manual state management where each state
 * needs to be explicitly instantiated with class information.
 */
interface PermissiveStateEnum : BaseStateEnum {
    val classz: KClass<out PermissiveDSL<*>>
}
