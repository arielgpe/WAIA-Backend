package main.kotlin.post

import io.javalin.Context
import io.javalin.apibuilder.CrudHandler

object PostController : CrudHandler {
    private val postDao = PostDao()

    override fun create(ctx: Context) {
        val post = ctx.body<Post>()
        postDao.save(content = post.content, ipAddress = ctx.ip())
        ctx.status(201)
    }

    override fun delete(ctx: Context, resourceId: String) {
        postDao.delete(id = resourceId)
        ctx.status(204)
    }

    override fun getAll(ctx: Context) {
        ctx.json(postDao.findAll())
    }

    override fun getOne(ctx: Context, resourceId: String) {
        ctx.json(postDao.findById(id = resourceId)!!)
    }

    override fun update(ctx: Context, resourceId: String) {
        val post = ctx.body<Post>()
        postDao.update(post = post, id = resourceId)
        ctx.status(204)
    }

}
