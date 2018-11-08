package main.kotlin

import com.mongodb.MongoClient
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.KMongo


open class MongoDriver {
    private val client: MongoClient = KMongo.createClient()
    var database: MongoDatabase

    init {
        database = client.getDatabase("WAIA")
    }

}