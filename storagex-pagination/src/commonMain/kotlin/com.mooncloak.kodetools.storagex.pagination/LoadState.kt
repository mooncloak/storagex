package com.mooncloak.kodetools.storagex.pagination

/**
 * LoadState of a Paged List load.
 */
public sealed interface LoadState {

    /**
     * `false` if there is more data to load, `true` otherwise. This parameter informs [Pager] if
     * it should continue to make requests for additional data in this direction or if it should
     * halt as the end of the dataset has been reached.
     */
    public val endOfPaginationReached: Boolean

    /**
     * Indicates the [Pager] is not currently loading, and no error currently observed.
     */
    public sealed interface NotLoading : LoadState

    /**
     * Completed version of [NotLoading].
     */
    public data object Complete : NotLoading {

        override val endOfPaginationReached: Boolean = true
    }

    /**
     * Incomplete version of [NotLoading].
     */
    public data object Incomplete : NotLoading {

        override val endOfPaginationReached: Boolean = false
    }

    /**
     * Loading is in progress.
     */
    public data object Loading : LoadState {

        override val endOfPaginationReached: Boolean = false
    }

    /**
     * Loading hit an error.
     *
     * @param cause [Throwable] that caused the load operation to generate this error state.
     */
    public data class Error public constructor(
        public val cause: Throwable
    ) : LoadState {

        override val endOfPaginationReached: Boolean = false
    }
}
