package dao

import javax.inject.{Inject, Singleton}
import models.Fry
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

trait FryComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's "frite" table in a object-oriented entity: the Fry model.
  class FryTable(tag: Tag) extends Table[Fry](tag, "frite") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // Primary key, auto-incremented
    def name = column[String]("nom")
    def portion = column[Int]("portion")
    def price = column[Double]("prix")

    // Map the attributes with the model.
    def * = (id.?, name, portion, price) <> (Fry.tupled, Fry.unapply)
  }
}

// This class contains the object-oriented list of fries and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the fry query table and loads the JDBC profile configured in the application's
// configuration file.
@Singleton
class FryDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends FryComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  // Get the object-oriented list of fries directly from the query table.
  val fries = TableQuery[FryTable]

  /** Retrieve the list of fries */
  def list(): Future[Seq[Fry]] = {
    val query = fries.sortBy(_.name)
    db.run(query.result)
  }

  /** Retrieve a fry from the id. */
  def findById(id: Long): Future[Option[Fry]] =
    db.run(fries.filter(_.id === id).result.headOption)

  /** Retrieve a fry from the name. */
  def findByName(name: String): Future[Option[Fry]] =
    db.run(fries.filter(_.name === name).result.headOption)

  /** Insert a new fry, then return it. */
  def insert(fry: Fry): Future[Fry] = {
    val insertQuery = fries returning fries.map(_.id) into ((fry, id) => fry.copy(Some(id)))
    db.run(insertQuery += fry)
  }

  /** Update a fry, then return an integer that indicate if the fry was found (1) or not (0). */
  def update(id: Long, fry: Fry): Future[Int] = {
    val fryToUpdate: Fry = fry.copy(Some(id))
    db.run(fries.filter(_.id === id).update(fryToUpdate))
  }

  /** Delete a fry, then return an integer that indicate if the fry was found (1) or not (0). */
  def delete(id: Long): Future[Int] =
    db.run(fries.filter(_.id === id).delete)
}
