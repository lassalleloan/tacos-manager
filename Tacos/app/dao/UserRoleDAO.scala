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
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc) // Primary key, auto-incremented
    def name = column[String]("name")

    // Map the attributes with the model
    def * = (id.?, name) <> (UserRole.tupled, UserRole.unapply)
  }
}

@Singleton
class UserRoleDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends UserRoleComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  // Get the object-oriented list of roles directly from the query table.
  val roles = TableQuery[UserRoleTable]

  /** Retrieve the list of roles */
  def list(): Future[Seq[UserRole]] = {
    val query = roles.sortBy(_.name)
    db.run(query.result)
  }

  /** Retrieve a role from the id. */
  def findById(id: Long): Future[Option[UserRole]] =
    db.run(roles.filter(_.id === id).result.headOption)

  /** Insert a new role, then return it. */
  def insert(role: UserRole): Future[UserRole] = {
    val insertQuery = roles returning roles.map(_.id) into ((role, id) => role.copy(Some(id)))
    db.run(insertQuery += role)
  }

  /** Update a role, then return an integer that indicate if the role was found (1) or not (0). */
  def update(id: Long, role: UserRole): Future[Int] = {
    val roleToUpdate: UserRole = role.copy(Some(id))
    db.run(roles.filter(_.id === id).update(roleToUpdate))
  }

  /** Delete a role, then return an integer that indicate if the role was found (1) or not (0). */
  def delete(id: Long): Future[Int] =
    db.run(roles.filter(_.id === id).delete)
}
