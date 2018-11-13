package waia.main.kotlin.user

import com.fasterxml.jackson.annotation.JsonIgnore
import mongo
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.*
import org.mindrot.jbcrypt.BCrypt
import waia.main.kotlin.utils.Roles
import waia.main.kotlin.utils.Status
import java.util.*


data class User (@BsonId val id: String = newId<User>().toString(),
                 val username: String = "",
                 var password: String = "",
                 val active: Boolean = true,
                 val role: String = Roles.ANYONE.name,
                 val createdTimestamp: Long = Calendar.getInstance().timeInMillis)


class UserDao {

    private val database = mongo().database
    private val users = database.getCollection<User>()

    fun save(newUser: User): Any {
        val user = findByUserName(newUser.username)
        if (user != null) {
            return Status(success = false, message = "User already exist")
        }
        newUser.password = BCrypt.hashpw(newUser.password,
                BCrypt.gensalt(12))
        users.insertOne(newUser)
        newUser.password = ""
        return newUser
    }

    fun login(userName: String, password: String): Status {
        val user = findByUserName(userName)
        if (user != null && BCrypt.checkpw(password, user.password)) {
            return Status(message = "Logged in!")
        }
        return Status(success = false, message = "Wrong username and password")
    }

    fun chancePassword(id: String, password: String): Status {
        users.updateOneById(id.toId<User>(),
                set(User::password, BCrypt.hashpw(password,
                        BCrypt.gensalt(12))
                )
        )
        return Status()
    }

    fun findAll(): MutableList<User> = users.find()
            .projection(exclude(User::password))
            .toMutableList()

    fun findById(id: String): User? = users.findOneById(id.toId<User>())

    fun findByUserName(userName: String): User? =
            users.findOne(User::username eq userName)

    fun update(id: String, user: User) {
        users.updateOneById(id.toId<User>(), set(User::javaClass, user))
    }

    fun delete(id: String): Status {
        users.deleteOneById(id.toId<User>())
        return Status()
    }

    fun deactivate(id: String, activate: Boolean): Status {
        users.updateOneById(id.toId<User>(), set(User::active, activate))
        val message = if (activate) "User activated" else "User deactivated"
        return Status(message = message)
    }

}