import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import models.Post
import models.PostDao

fun main(args: Array<String>) {
    val postDao = PostDao()

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


        get("/posts") { ctx ->
            ctx.json(postDao.posts)
        }
        get("/posts/:id") { ctx ->
            val postId = ctx.pathParam("id").toInt()
            ctx.json(postDao.findById(postId)!!)
        }
        post("/posts"){ ctx ->
            val post = ctx.body<Post>()
            postDao.save(post.content, "")
            ctx.status(201)
        }
        patch("/posts/:id"){ ctx ->
            val post = ctx.body<Post>()
            postDao.update(post)
            ctx.status(204)
        }
        delete("/posts/:id"){ ctx ->
            val postId = ctx.pathParam("id").toInt()
            postDao.delete(postId)
            ctx.status(204)
        }

    }

}



