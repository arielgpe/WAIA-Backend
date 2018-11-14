package waia.main.kotlin.post

import com.mongodb.lang.Nullable
import mongo
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.*
import waia.main.kotlin.user.User
import waia.main.kotlin.user.UserDao
import waia.main.kotlin.utils.Status
import java.util.*

data class Post(@BsonId val id: String = newId<Post>().toString(),
                var name: String = "Anon",
                val content: String,
                val ipAddress: String = "0.0.0.0",
                @Nullable val user_id: Id<User>? = "".toId(),
                val createdTimestamp: Long = Calendar.getInstance().timeInMillis)

class PostDao {
    private val database = mongo().database
    private val posts = database.getCollection<Post>()

    fun save(content: String, ipAddress: String, user_id: String?): Post {
        val post = Post(content = content, ipAddress = ipAddress)
        val user = findUser(user_id!!)
        if (user != null) {
            post.name = user.username
        }
        posts.insertOne(post)
        return post
    }

    fun findAll(): MutableList<Post> = posts.find<Post>()
            .toMutableList()

    fun findById(id: String): Post? =
            posts.findOneById(id.toId<Post>())

    fun update(id: String, post: Post) {
        posts.updateOneById(id.toId<Post>(), set(Post::content, post.content))
    }

    fun delete(id: String): Status {
        posts.deleteOneById(id.toId<Post>())
        return Status()
    }

    fun findUser(user_id: String): User?{
        val userDao = UserDao()
        return userDao.findById(user_id)
    }
}