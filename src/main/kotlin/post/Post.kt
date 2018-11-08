package post

import java.util.concurrent.atomic.AtomicInteger

data class Post(val id: Int, val name: String = "Anon", val content: String, val likes: Int = 0,
                val ipAddress: String = "0.0.0.0", val createdDate: Int = 0)

class PostDao {

    val posts = mutableListOf(
            Post(id = 1, content = "Test Post", ipAddress = "0.0.0.0"),
            Post(id = 2, content = "I hate when my post dont work", ipAddress = "0.0.0.0"),
            Post(id = 3, content = "Why do i have to make DAOS", ipAddress = "0.0.0.0"),
            Post(id = 4, content = "Yeah i get it whatever", ipAddress = "0.0.0.0"),
            Post(id = 5, content = "ffs", ipAddress = "0.0.0.0")
    )

    var lastId: AtomicInteger = AtomicInteger(posts.size)

    fun save(content: String, ipAddress: String){
        val id = lastId.incrementAndGet()
        posts.add(Post(id = id, content = content))
    }

    fun findById(id: Int): Post? {
        return posts.find { it -> it.id == id}
    }

    fun update(post: Post) {
        posts[post.id - 1] = post
    }

    fun delete(id: Int){
        posts.remove(findById(id))
    }
}