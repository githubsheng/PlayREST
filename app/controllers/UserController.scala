package controllers

import javax.inject._
import play.api._
import play.api.libs.json.Json
import play.api.libs.json.Writes
import play.api.mvc._
import services.User
import services.UserRepo
import play.api.libs.json.JsPath
import play.api.libs.json.Reads
import play.api.libs.json.JsError
import play.api.libs.functional.syntax._

@Singleton
class UserController @Inject() (userRepo: UserRepo) extends Controller {

  implicit val userReads: Reads[User] = (
    (JsPath \ "id").read[Long] and
    (JsPath \ "firstName").read[String] and
    (JsPath \ "lastName").read[String] and
    (JsPath \ "age").read[Int])(User.apply _)

  implicit val userWrites = new Writes[User] {
    def writes(user: User) = Json.obj(
      "id" -> user.id,
      "firstName" -> user.firstName,
      "lastName" -> user.lastName,
      "age" -> user.age)
  }

  def getUsers = Action {
    val users = userRepo.getAllUsers()
    val json = Json.toJson(users)
    Ok(json)
  }

  def addUser = Action(BodyParsers.parse.json) { request =>
    val userResult = request.body.validate[User]
    userResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      user => {
        userRepo.addUser(user)
        Ok(Json.obj("status" -> "OK", "message" -> ("User saved.")))
      })
  }

  def getUser(id: Long) = Action {
    val user: User = userRepo.getUser(id)
    if(user != null) {
      val json = Json.toJson(user)
      Ok(json)      
    } else {
      Ok("no such user")
    }

  }

}
