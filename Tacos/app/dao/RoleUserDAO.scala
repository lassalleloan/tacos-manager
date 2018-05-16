package dao

import javax.inject.{Inject, Singleton}
import models.RoleUser
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

// We use a trait component here in order to share the RoleTable class with other DAO, thanks to the inheritance.
trait RoleUserComponent extends UserComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's role table in a object-oriented entity: the Role model.
  class RoleUserTable(tag: Tag) extends Table[RoleUser](tag, "roleUser") {
    def userId = column[Long]("userId")
    def roleId = column[Long]("roleId")

    // Map the attributes with the model
    def * = (userId, roleId) <> (RoleUser.tupled, RoleUser.unapply)
  }
}

@Singleton
class RoleUserDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends RoleUserComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  // Get the object-oriented list of users-roles directly from the query table.
  val usersRoles = TableQuery[RoleUserTable]

  /** Retrieve the list of roles sorted by name */
  def list(): Future[Seq[RoleUser]] = {
    db.run(usersRoles.result)
  }
}
