package waia.main.kotlin

import com.mongodb.MongoClient
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.KMongo


object MongoDriver : MongoAdapter {
    override val client: MongoClient
        get() = KMongo.createClient()
    override val database: MongoDatabase
        get() = client.getDatabase("WAIA")
}

interface MongoAdapter {
    val client: MongoClient
    val database: MongoDatabase
}