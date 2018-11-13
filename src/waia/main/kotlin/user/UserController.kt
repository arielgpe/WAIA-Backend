package waia.main.kotlin.user

import io.javalin.Context
import io.javalin.apibuilder.CrudHandler
import waia.main.kotlin.utils.Status


object UserController: CrudHandler {
    private val userDao = UserDao()


    override fun create(ctx: Context) {
        val user = ctx.body<User>()
        val newUser = userDao.save(user)
        ctx.json(newUser).status(if (newUser is Status) 400 else 200)
    }

    fun login(ctx: Context){
        val user = ctx.body<User>()
        val status = userDao.login(user.username, user.password)
        if (status.success) ctx.status(200).json(status)
        else ctx.status(401).json(status)
    }

    override fun delete(ctx: Context, resourceId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAll(ctx: Context) {
        ctx.json(userDao.findAll())
    }

    override fun getOne(ctx: Context, resourceId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(ctx: Context, resourceId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
