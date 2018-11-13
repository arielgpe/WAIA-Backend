package waia.main.kotlin

import com.mongodb.MongoClient
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.KMongo


object MongoDriver : MongoAdapter {
    private val client = client()

    override val database: MongoDatabase
        get() = client.getDatabase("WAIA")

}

interface MongoAdapter {
    val database: MongoDatabase

    fun client(): MongoClient {
       return KMongo.createClient()
    }
}