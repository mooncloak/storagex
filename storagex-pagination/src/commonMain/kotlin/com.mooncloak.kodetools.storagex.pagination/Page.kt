package com.mooncloak.kodetools.storagex.pagination

/**
 * Represents a generic page of data. A [Page] can be a resolved page of data which contains the
 * actual page data (via [ResolvedPage]), a collection of resolved pages (via [PageCollection]), or
 * a placeholder of data which can be lazily fetched (via [PagePlaceholder]).
 */
@ExperimentalPaginationAPI
public sealed interface Page<Item> : PageLoadResult<Item> {

    /**
     * A unique identifier value for this [ResolvedPage] instance.
     */
    public val id: String

    /**
     * A unique identifier value for the data source. This value is especially important when you
     * have numerous data sources that are being merged into a single source (ex: using
     * [PageCollection]), as it provides a way to load the next page of data from the appropriate
     * data source.
     */
    public val dataSourceId: String?

    /**
     * A [Cursor] which is an opaque [String] value from the caller's
     * perspective. From the implementor's perspective, this is a serialized object value
     * containing data necessary for the (re-)loading of the page data. This value could be a
     * serialized form of a [Cursor] and [Direction], or contain more complex data, it is up to the
     * implementation to decide what data to store.
     */
    public val pageCursor: Cursor?

    /**
     * Retrieves the underlying [List] of data items for this page.
     */
    public suspend fun get(): List<Item>

    public companion object
}

/**
 * Represents a resolved [Page] of data.
 *
 * @property [dataSourceId] A unique identifier value for the data source. This value is especially
 * important when you have numerous data sources that are being merged into a single source
 * (ex: using [PageCollection]), as it provides a way to load the next page of data from the
 * appropriate data source.
 *
 * @property [id] A unique identifier value for this [ResolvedPage] instance.
 *
 * @property [pageCursor] A [Cursor] which is an opaque [String] value from the caller's
 * perspective. From the implementor's perspective, this is a serialized object value containing
 * data necessary for the (re-)loading of the page data. This value could be a serialized form of a
 * [Cursor] and [Direction], or contain more complex data, it is up to the implementation to decide
 * what data to store.
 *
 * @property [info] The [PageInfo] for this page of data.
 *
 * @property [items] The [List] of data items for this page.
 */
@ExperimentalPaginationAPI
public interface ResolvedPage<Item> : Page<Item> {

    public override val id: String

    public override val dataSourceId: String?

    public override val pageCursor: Cursor? get() = null

    public val info: PageInfo

    public val items: List<Item>

    public override suspend fun get(): List<Item> = items

    public companion object
}

/**
 * Represents a collection of [Page]s collectively representing a single loaded [Page]. This could
 * be useful for when paginating through multiple sources of data. For instance, consider a local
 * source of one data set and a remote source of another data set, both of which are to be combined
 * into a single data set. With this approach, a [PageCollection] can be used to represent a single
 * [Page] from the combined data set, which contains a [Page] from each of the local and remote
 * data sets. This way, the loading of the individual [Page]s is data source specific, but the
 * overall pagination of the whole data can be represented with this [PageCursor] model.
 *
 * @property [dataSourceId] A unique identifier value for the data source. This value is especially
 * important when you have numerous data sources that are being merged into a single source
 * (ex: using [PageCollection]), as it provides a way to load the next page of data from the
 * appropriate data source.
 *
 * @property [pages] The [ResolvedPage]s in this [PageCollection].
 *
 * @property [pageCursor] A [Cursor] which is an opaque [String] value from the caller's
 * perspective. From the implementor's perspective, this is a serialized object value containing
 * data necessary for the (re-)loading of the page data. This value could be a serialized form of a
 * [Cursor] and [Direction], or contain more complex data, it is up to the implementation to decide
 * what data to store.
 */
@ExperimentalPaginationAPI
public interface PageCollection<Item> : Page<Item> {

    public override val id: String

    public override val dataSourceId: String?

    public override val pageCursor: Cursor

    public val pages: List<ResolvedPage<Item>>

    public override suspend fun get(): List<Item> =
        pages.flatMap { page -> page.get() }

    public companion object
}

/**
 * Represents a placeholder for a [Page]. For instance, when loading from a very large data set, it
 * may be desirable to only store a certain amount of pages in memory at a time to prevent
 * out-of-memory exceptions and slow applications. One way to continue to paginate from a large
 * data set and support only the most recent pages in memory at a time, is to use a
 * [PagePlaceholder], where the corresponding [Page] can be loaded when it needs to be displayed.
 * The data associated with the [PagePlaceholder] may be persisted to disk, in-memory, or require
 * an HTTP request to reload. How the data is stored and reloaded is an implementation specific
 * detail.
 *
 * > [!Note]
 * > For supporting a very, very large data set, consider using out-of-order loading of pages
 * > instead of an in-order [List] of [Page]s. That will allow completely removing older pages from
 * > memory when they are no longer needed.
 *
 * @property [dataSourceId] A unique identifier value for the data source. This value is especially
 * important when you have numerous data sources that are being merged into a single source
 * (ex: using [PageCollection]), as it provides a way to load the next page of data from the
 * appropriate data source.
 *
 * @property [pageCursor] A [Cursor] which is an opaque [String] value from the caller's
 * perspective. From the implementor's perspective, this is a serialized object value containing
 * data necessary for the (re-)loading of the page data. This value could be a serialized form of a
 * [Cursor] and [Direction], or contain more complex data, it is up to the implementation to decide
 * what data to store.
 */
@ExperimentalPaginationAPI
public interface PagePlaceholder<Item> : Page<Item> {

    public override val id: String

    public override val dataSourceId: String?

    public override val pageCursor: Cursor?

    public suspend fun resolve(): ResolvedPage<Item>

    public override suspend fun get(): List<Item> =
        resolve().get()

    public companion object
}

@ExperimentalPaginationAPI
public suspend inline fun <Item> Page<Item>.toResolvedPage(): ResolvedPage<Item> =
    when (this) {
        is ResolvedPage<Item> -> this

        is PageCollection<Item> -> ResolvedPage(
            id = this.id,
            items = this.get(),
            dataSourceId = this.dataSourceId,
            info = PageInfo()
        )

        is PagePlaceholder<Item> -> this.resolve()
    }
