package io.lb.middleware.common.shared.middleware.request

/**
 * Enum class representing the HTTP methods that middleware can handle.
 *
 * @property Delete The DELETE HTTP method.
 * @property Get The GET HTTP method.
 * @property Head The HEAD HTTP method.
 * @property Patch The PATCH HTTP method.
 * @property Post The POST HTTP method.
 * @property Put The PUT HTTP method.
 */
enum class MiddlewareHttpMethods {
    Delete,
    Get,
    Head,
    Patch,
    Post,
    Put,
}
