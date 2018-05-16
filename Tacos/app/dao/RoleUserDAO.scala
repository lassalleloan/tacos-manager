package dao

import javax.inject.{Inject, Singleton}
import models.RoleUser
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

trait RoleUserComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's roleUser table in a object-oriented entity: the RoleUser model.
  class RoleUserTable(tag: Tag) extends Table[RoleUser](tag, "rolePersonne") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // Primary key, auto-incremented
    def name = column[String]("name")

    // Map the attributes with the model
    def * = (id, name) <> (RoleUser.tupled, RoleUser.unapply)
  }
}

@Singleton
class RoleUserDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends RoleUserComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  // Get the object-oriented list of roles directly from the query table.
  val roles = TableQuery[RoleUserTable]

  /** Retrieve the list of roles sorted by name */
  def list(): Future[Seq[RoleUser]] = {
    db.run(roles.result)
  }
}