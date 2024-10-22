package com.mooncloak.kodetools.storagex.pagination

/**
 * An annotation used to mark a pagination component as experimental.
 *
 * Components annotated with this annotation are considered experimental and may contain errors and
 * the API may change in the future. Use those APIs with caution. Components annotated with
 * [ExperimentalPaginationAPI] will require an explicit opt-in.
 */
@RequiresOptIn(
    message = "This is an experimental API and it can break or change in the future. Use with caution.",
    level = RequiresOptIn.Level.WARNING
)
@MustBeDocumented
public annotation class ExperimentalPaginationAPI public constructor()
