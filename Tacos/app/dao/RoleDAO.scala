package dao

import javax.inject.{Inject, Singleton}
import models.Role
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

// We use a trait component here in order to share the RoleTable class with other DAO, thanks to the inheritance.
trait RoleComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's role table in a object-oriented entity: the Role model.
  class RoleTable(tag: Tag) extends Table[Role](tag, "role") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc) // Primary key, auto-incremented
    def name = column[String]("name")

    // Map the attributes with the model; the ID is optional.
    def * = (id.?, name) <> (Role.tupled, Role.unapply)
  }
}

// This class contains the object-oriented list of roles and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the role's query table and loads the JDBC profile configured in the application's
// configuration file.
@Singleton
class RoleDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends RoleComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  // Get the object-oriented list of roles directly from the query table.
  val roles = TableQuery[RoleTable]

  /** Retrieve the list of roles */
  def list(): Future[Seq[Role]] = {
    val query = roles.sortBy(_.name)
    db.run(query.result)
  }

  /** Retrieve a role from the id. */
  def findById(id: Long): Future[Option[Role]] =
    db.run(roles.filter(_.id === id).result.headOption)

  /** Insert a new role, then return it. */
  def insert(role: Role): Future[Role] = {
    val insertQuery = roles returning roles.map(_.id) into ((role, id) => role.copy(Some(id)))
    db.run(insertQuery += role)
  }

  /** Update a role, then return an integer that indicate if the role was found (1) or not (0). */
  def update(id: Long, role: Role): Future[Int] = {
    val roleToUpdate: Role = role.copy(Some(id))
    db.run(roles.filter(_.id === id).update(roleToUpdate))
  }

  /** Delete a role, then return an integer that indicate if the role was found (1) or not (0). */
  def delete(id: Long): Future[Int] =
    db.run(roles.filter(_.id === id).delete)
}
