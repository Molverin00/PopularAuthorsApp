package com.example.popularauthorsapp.util

/**
 * A sealed class representing a resource that encapsulates the status of an operation along with its data or error.
 *
 * @param T The type of data contained in the resource.
 * @property data The data associated with the resource.
 * @property error The error associated with the resource, if any.
 */
sealed class Resource<T>(
    val data: T? = null,
    val error: Throwable? = null
) {

    /**
     * Represents a successful resource with the associated data.
     *
     * @param T The type of data contained in the resource.
     * @property data The data associated with the resource.
     */
    class Success<T>(data: T) : Resource<T>(data)

    /**
     * Represents a loading state resource, indicating that the operation is in progress.
     *
     * @param T The type of data contained in the resource.
     * @property data The data associated with the resource, if any.
     */
    class Loading<T>(data: T? = null) : Resource<T>(data)

    /**
     * Represents an error state resource with the associated error and optional data.
     *
     * @param T The type of data contained in the resource.
     * @property error The error associated with the resource.
     * @property data The data associated with the resource, if any.
     */
    class Error<T>(throwable: Throwable, data: T? = null) : Resource<T>(data, throwable)
}
