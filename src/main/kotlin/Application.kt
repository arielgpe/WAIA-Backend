import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.crud
import io.javalin.apibuilder.ApiBuilder.get
import post.PostController

fun main(args: Array<String>) {

    val app = Javalin.create()
            .apply {
                error(404) { ctx ->
                    ctx.json("Not found")
                }
            }
            .contextPath("/api")
            .enableRouteOverview("/overview")
            .start(3000)

    app.routes {
        get("/") { ctx ->
            ctx.result("Hello World")
        }

        crud("/posts/:id", PostController)
    }

}



