package dao

import javax.inject.{Inject, Singleton}
import models.UserRole
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

// We use a trait component here in order to share the RoleTable class with other DAO, thanks to the inheritance.
trait UserRoleComponent extends UserComponent with RoleComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's role table in a object-oriented entity: the Role model.
  class UserRoleTable(tag: Tag) extends Table[UserRole](tag, "userRole") {
    def userId = column[Long]("userId")
    def roleId = column[Long]("roleId")

    // Map the attributes with the model
    def * = (userId, roleId) <> (UserRole.tupled, UserRole.unapply)
  }
}

@Singleton
class UserRoleDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends UserRoleComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  // Get the object-oriented list of users-roles directly from the query table.
  val usersRoles = TableQuery[UserRoleTable]

  /** Retrieve the list of roles sorted by name */
  def list(): Future[Seq[UserRole]] = {
    db.run(usersRoles.result)
  }
}
