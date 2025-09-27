package net.botwithus.kxapi.permissive

/**
 * Base interface for all state enums that provides the description property.
 * This allows StateIterator to work with both StateEnum and PermissiveStateEnum.
 */
interface BaseStateEnum {
    val description: String
}
