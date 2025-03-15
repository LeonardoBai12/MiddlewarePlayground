package io.lb.middleware.impl.database.factory

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import io.lb.middleware.database.MiddlewareDatabase

actual class DatabaseDriverFactory {
    actual fun create(): SqlDriver {
        return NativeSqliteDriver(MiddlewareDatabase.Schema, "middleware.db")
    }
}