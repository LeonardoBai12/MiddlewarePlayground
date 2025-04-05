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
enum class MiddlewareHttpMethods(val color: Long) {
    Get(0xFF00C853),
    Post(0xFF2979FF),
    Patch(0xFF651FFF),
    Put(0xFF00A4B6),
    Delete(0xFFD500000),
    Head(0xFFFF6D00),
}
