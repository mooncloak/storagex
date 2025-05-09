package com.mooncloak.kodetools.storagex.skema

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MetaSerializable
import kotlinx.serialization.SerialInfo
import kotlin.reflect.KClass

public annotation class Schema(
    val name: String = "",
    val version: Int = 1,
    vararg val entities: KClass<*>
)


@OptIn(ExperimentalSerializationApi::class)
@MetaSerializable
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
public annotation class Persistable()


@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
public annotation class Id() // Database UUID (ex: String)


@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
public annotation class LocalId() // Local database ID (ex: Int)


@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
public annotation class Timestamp() // Created vs Updated


@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
public annotation class Embedded()


@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
public annotation class Referenced(vararg val by: KClass<*>)


@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
public annotation class PersistedName(val name: String)
