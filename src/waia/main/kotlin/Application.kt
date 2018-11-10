import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.security.SecurityUtil.roles
import waia.main.kotlin.MongoAdapter
import waia.main.kotlin.MongoDriver
import waia.main.kotlin.banned.BanController
import waia.main.kotlin.post.PostController
import waia.main.kotlin.user.UserController
import waia.main.kotlin.utils.Roles
import waia.main.kotlin.utils.Status
import waia.main.kotlin.utils.getUserRole

fun mongo(): MongoAdapter = MongoDriver

fun main(args: Array<String>) {
    val app = Javalin.create()
            .apply {
                error(404) { ctx ->
                    ctx.json("Not found")
                }
                contextPath("/api")
                enableRouteOverview("/overview")
            }
            .accessManager { handler, ctx, permittedRoles ->
                val userRole = getUserRole(ctx)
                if (permittedRoles.contains(userRole)) {
                    handler.handle(ctx)
                } else {
                    ctx.status(401).json(Status(success = false, message = "Unauthorized"))
                }
            }
            .start(3001)


    app.routes {
        get("/") { ctx ->
            ctx.result("Hello World")
        }

        //Posts
        crud("/posts/:id", PostController, roles(Roles.ANYONE))

        //Users
        path("users"){
            get(UserController::getAll, roles(Roles.ANYONE))
            post(UserController::create, roles(Roles.ANYONE))
            path("login"){
                post(UserController::login, roles(Roles.ANYONE))
            }
        }


        //Bans
        path("bans") {
            get(BanController::getAll, roles(Roles.ADMIN))
            post(BanController::create, roles(Roles.ADMIN))
            path(":ip") {
                get(BanController::getOne, roles(Roles.ADMIN, Roles.ANYONE))
                delete(BanController::delete, roles(Roles.ADMIN))
            }
        }
    }

}



