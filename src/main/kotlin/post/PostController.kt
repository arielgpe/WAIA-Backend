package post

import io.javalin.Context
import io.javalin.apibuilder.CrudHandler

object PostController: CrudHandler{
    private val postDao = PostDao()

    override fun create(ctx: Context) {
        val post = ctx.body<Post>()
        postDao.save(post.content, "")
        ctx.status(201)
    }

    override fun delete(ctx: Context, resourceId: String) {
        val postId = ctx.pathParam("id").toInt()
        postDao.delete(postId)
        ctx.status(204)
    }

    override fun getAll(ctx: Context) {
        ctx.json(postDao.posts)
    }

    override fun getOne(ctx: Context, resourceId: String) {
        val postId = ctx.pathParam("id").toInt()
        ctx.json(postDao.findById(postId)!!)
    }

    override fun update(ctx: Context, resourceId: String) {
        val post = ctx.body<Post>()
        postDao.update(post)
        ctx.status(204)
    }

}
