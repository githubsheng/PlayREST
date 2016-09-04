package services

import java.util.concurrent.ConcurrentHashMap

import javax.inject._
import scala.collection.JavaConversions._
import scala.collection.JavaConversions

trait UserRepo {
  def getAllUsers(): List[User]
  def getUser(id: Long): User
  def addUser(user: User)
}

case class User(id: Long, firstName: String, lastName: String, age: Int)

@Singleton
class UserRepoImpl extends UserRepo {

  val userMap = new ConcurrentHashMap[Long, User]()

  override def getAllUsers(): List[User] = {
    val users = userMap.values()
    //not sure what's the best practice of converting a Java collection to Scala list.
    val buf = scala.collection.mutable.ListBuffer.empty[User]
    for(user <- users) buf += user
    return buf.toList
  }

  override def getUser(id: Long): User = { userMap.get(id) }

  override def addUser(user: User) = { userMap.put(user.id, user) }

}