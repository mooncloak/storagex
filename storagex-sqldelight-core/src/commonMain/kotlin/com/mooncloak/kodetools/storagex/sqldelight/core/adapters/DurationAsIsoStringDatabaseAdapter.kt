package com.mooncloak.kodetools.storagex.sqldelight.core.adapters

import app.cash.sqldelight.ColumnAdapter
import kotlin.time.Duration

/**
 * A [ColumnAdapter] implementation that stores [Duration] values as ISO-8601 [String] values in the
 * database.
 *
 * @see [DatabaseAdapter.Companion.durationAsIsoString] for accessing this object.
 */
public object DurationAsIsoStringDatabaseAdapter : DatabaseAdapter<Duration, String> {

    override fun decode(databaseValue: String): Duration =
        Duration.parse(databaseValue)

    override fun encode(value: Duration): String =
        value.toIsoString()
}
