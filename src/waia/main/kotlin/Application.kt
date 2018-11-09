import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import waia.main.kotlin.MongoAdapter
import waia.main.kotlin.MongoDriver
import waia.main.kotlin.banned.BanController
import waia.main.kotlin.post.PostController

fun mongo(): MongoAdapter = MongoDriver

fun main(args: Array<String>) {
    val app = Javalin.create()
            .apply {
                error(404) { ctx ->
                    ctx.json("Not found")
                }
            }
            .contextPath("/api")
            .enableRouteOverview("/overview")
            .start(3001)

    app.routes {
        get("/") { ctx ->
            ctx.result("Hello World")
        }

        //Posts
        crud("/posts/:id", PostController)

        //Bans
        path("bans"){
            get(BanController::getAll)
            post(BanController::create)
            path(":ip"){
                get(BanController::getOne)
                delete(BanController::delete)
            }
        }
    }

}
