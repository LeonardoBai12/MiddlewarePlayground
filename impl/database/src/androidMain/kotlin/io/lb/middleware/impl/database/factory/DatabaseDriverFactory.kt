package io.lb.middleware.impl.database.factory

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.lb.middleware.impl.client.MiddlewareDatabase

actual class DatabaseDriverFactory (
    private val context: Context
) {
    actual fun create(): SqlDriver {
        return AndroidSqliteDriver(
            MiddlewareDatabase.Schema, context, "middleware.sq"
        )
    }
}
