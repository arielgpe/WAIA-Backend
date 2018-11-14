package waia.main.kotlin.utils

import mongo
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
import waia.main.kotlin.user.User

fun currentUser(accessToken: String): User? = mongo().database.getCollection<User>().findOne()