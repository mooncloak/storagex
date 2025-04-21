package com.mooncloak.kodetools.storagex.sqldelight.core.adapters

import app.cash.sqldelight.ColumnAdapter
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * A [ColumnAdapter] implementation that stores [Duration] values as millisecond [Long] values in the database.
 *
 * @see [DatabaseAdapter.Companion.durationAsMillisecondsLong] for accessing this object.
 */
public object DurationAsMillisecondsLongDatabaseAdapter : DatabaseAdapter<Duration, Long> {

    override fun decode(databaseValue: Long): Duration =
        databaseValue.milliseconds

    override fun encode(value: Duration): Long =
        value.inWholeMilliseconds
}
