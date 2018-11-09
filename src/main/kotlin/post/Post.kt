package main.kotlin.post

import main.kotlin.MongoDriver
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.*

data class Post(@BsonId val id: String = newId<String>().toString(), val name: String = "Anon", val content: String,
                val ipAddress: String = "0.0.0.0")

class PostDao: MongoDriver() {
    private val posts = database.getCollection<Post>()

    fun save(content: String, ipAddress: String){
        posts.insertOne(Post(content = content, ipAddress = ipAddress))
    }

    fun findAll(): MutableList<Post>{
        return posts.find<Post>().toMutableList()
    }

    fun findById(id: String): Post? {
        return posts.findOneById(id.toId<Post>())
    }

    fun update(id: String, post: Post) {
        posts.updateOneById(id.toId<Post>(), set(Post::content, post.content))
    }

    fun delete(id: String){
        posts.deleteOneById(id.toId<Post>())
    }
}