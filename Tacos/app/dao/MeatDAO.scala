package dao

import javax.inject.{Inject, Singleton}

import models.Meat
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

trait MeatComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's "viande" table in a object-oriented entity: the Meat model.
  class MeatTable(tag: Tag) extends Table[Meat](tag, "viande") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // Primary key, auto-incremented
    def name = column[String]("nom")
    def origin = column[String]("provenance")

    // Map the attributes with the model.
    def * = (id, name, origin) <> (Meat.tupled, Meat.unapply)
  }
}

// This class contains the object-oriented list of users and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the user query table and loads the JDBC profile configured in the application's
// configuration file.
@Singleton
class MeatDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends MeatComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  // Get the object-oriented list of meats directly from the query table.
  val meats = TableQuery[MeatTable]

  /** Retrieve the list of meats sorted by name */
  def list(): Future[Seq[Meat]] = {
    val query = meats.sortBy(_.name)
    db.run(query.result)
  }

  /** Retrieve a meat from the id. */
  def findById(id: Long): Future[Option[Meat]] =
    db.run(meats.filter(_.id === id).result.headOption)

  /** Retrieve a meat from the name. */
  def findByName(name: String): Future[Option[Meat]] =
    db.run(meats.filter(_.name === name).result.headOption)

  /** Insert a new meat, then return it. */
  def insert(meat: Meat): Future[Meat] = {
    val insertQuery = meats returning meats.map(_.id) into ((meat, id) => meat.copy(id))
    db.run(insertQuery += meat)
  }

  /** Update a meat, then return an integer that indicate if the meat was found (1) or not (0). */
  def update(id: Long, meat: Meat): Future[Int] = {
    val meatToUpdate: Meat = meat.copy(id)
    db.run(meats.filter(_.id === id).update(meatToUpdate))
  }

  /** Delete a meat, then return an integer that indicate if the meat was found (1) or not (0). */
  def delete(id: Long): Future[Int] =
    db.run(meats.filter(_.id === id).delete)
}