package main.kotlin.utils

import io.javalin.Context
import io.javalin.security.Role


fun getUserRole(ctx: Context): Role {
    val role = ctx.header("Role")
    return if (role != null) Roles.valueOf(role) else Roles.ANYONE
}

internal enum class Roles : Role {
    ANYONE, ADMIN, BANNED
}