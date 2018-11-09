package waia.main.kotlin.banned

import mongo
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
import org.litote.kmongo.newId
import waia.main.kotlin.utils.Status
import java.util.*

data class Ban(@BsonId val id: String = newId<Ban>().toString(), val ipAddress: String, val createdTimestamp: Long = Calendar.getInstance().timeInMillis)

class BanDao {
    private val database = mongo().database
    private val bans = database.getCollection<Ban>()

    fun save(ipAddress: String): Status {
        val ban = findByIpAddress(ipAddress)
        if (ban == null){
            bans.insertOne(Ban(ipAddress = ipAddress))
            return Status(message = "Successfully created")
        }
        return Status(success = false, message = "IP Address already banned")
    }

    fun findAll(): MutableList<Ban> = bans.find().toMutableList()

    fun findByIpAddress(ipAddress: String): Ban? =
            bans.findOne(Ban::ipAddress eq ipAddress)

    fun delete(ipAddress: String): Status {
        bans.deleteOne(Ban::ipAddress eq ipAddress)
        return Status()
    }
}