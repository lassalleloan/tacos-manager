package dao

import javax.inject.{Inject, Singleton}
import models.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

// We use a trait component here in order to share the user User class with other DAO, thanks to the inheritance.
trait UserComponent extends RoleUserComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's "personne" table in a object-oriented entity: the User model.
  class UserTable(tag: Tag) extends Table[User](tag, "personne") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // Primary key, auto-incremented
    def firstName = column[String]("prenom")
    def lastName = column[String]("nom")
    def phone = column[String]("telephone")
    def email = column[String]("mail", O.Unique) // Unique
    def password = column[String]("mdp")
    def roleUser = column[Long]("rolePersonne_fk")

    // Map the attributes with the model. Phone is optional.
    def * = (id.?, firstName, lastName, phone.?, email, password, roleUser) <> ((User.apply _).tupled, User.unapply)
  }

}

// This class contains the object-oriented list of users and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the user query table and loads the JDBC profile configured in the application's
// configuration file.
@Singleton
class UserDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends UserComponent with RoleUserComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  // Get the object-oriented list of users directly from the query table.
  val users = TableQuery[UserTable]

  /** Retrieve the list of users sorted by lastName and firstName */
  def list(): Future[Seq[User]] = {
    val query = users.sortBy(x => (x.lastName, x.firstName))
    db.run(query.result)
  }

  /** Retrieve a user from the id. */
  def findById(id: Long): Future[Option[User]] =
    db.run(users.filter(_.id === id).result.headOption)

  /** Retrieve a user from the email. */
  def findByEmail(email: String): Future[Option[User]] =
    db.run(users.filter(_.email === email).result.headOption)

  /** Insert a new user, then return it. */
  def insert(user: User): Future[User] = {
    val insertQuery = users returning users.map(_.id) into ((user, id) => user.copy(Some(id)))
    db.run(insertQuery += user)
  }

  /** Update a user, then return an integer that indicate if the user was found (1) or not (0). */
  def update(id: Long, user: User): Future[Int] = {
    val userToUpdate: User = user.copy(Some(id))
    db.run(users.filter(_.id === id).update(userToUpdate))
  }

  /** Delete a user, then return an integer that indicate if the user was found (1) or not (0). */
  def delete(id: Long): Future[Int] =
    db.run(users.filter(_.id === id).delete)
}