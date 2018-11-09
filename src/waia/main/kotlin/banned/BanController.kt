package waia.main.kotlin.banned

import io.javalin.Context
import waia.main.kotlin.utils.Status

object BanController {

    private val banDao = BanDao()

    fun create(ctx: Context){
        val ban = ctx.body<Ban>()
        val status = banDao.save(ipAddress = ban.ipAddress)
        ctx.status(if (status.success) 200 else 400)
        ctx.json(status)
    }

    fun delete(ctx: Context){
        val resourceId = ctx.pathParam("ip")
        val status = banDao.delete(resourceId)
        ctx.json(status)
    }

    fun getAll(ctx: Context){
        ctx.json(banDao.findAll())
    }

    fun getOne(ctx: Context){
        val resourceId = ctx.pathParam("ip")
        val ban = banDao.findByIpAddress(ipAddress = resourceId)
        if (ban != null){
            ctx.json(ban)
        } else {
            ctx.json(Status(success = false, message = "Not found"))
        }
    }
}