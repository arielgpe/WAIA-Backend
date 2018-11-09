package waia.main.kotlin.post

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.*
import waia.main.kotlin.MongoDriver
import waia.main.kotlin.utils.Status
import java.util.*

data class Post(@BsonId val id: String = newId<Post>().toString(), val name: String = "Anon", val content: String,
                val ipAddress: String = "0.0.0.0", val createdTimestamp: Long = Calendar.getInstance().timeInMillis)

class PostDao : MongoDriver() {
    private val posts = database.getCollection<Post>()

    fun save(content: String, ipAddress: String): Post {
        val post = Post(content = content, ipAddress = ipAddress)
        posts.insertOne(post)
        return post
    }

    fun findAll(): MutableList<Post> = posts.find<Post>().toMutableList()

    fun findById(id: String): Post? =
            posts.findOneById(id.toId<Post>())

    fun update(id: String, post: Post) {
        posts.updateOneById(id.toId<Post>(), set(Post::content, post.content))
    }

    fun delete(id: String): Status {
        posts.deleteOneById(id.toId<Post>())
        return Status()
    }
}