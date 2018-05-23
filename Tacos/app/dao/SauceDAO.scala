package dao

import javax.inject.{Inject, Singleton}

import models.Sauce
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

trait SauceComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's "sauce" table in a object-oriented entity: the Sauce model.
  class SauceTable(tag: Tag) extends Table[Sauce](tag, "sauce") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // Primary key, auto-incremented
    def name = column[String]("nom")
    def spicy = column[Int]("puissance")

    // Map the attributes with the model.
    def * = (id, name, spicy) <> (Sauce.tupled, Sauce.unapply)
  }
}

// This class contains the object-oriented list of users and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the user query table and loads the JDBC profile configured in the application's
// configuration file.
@Singleton
class SauceDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends SauceComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  // Get the object-oriented list of sauces directly from the query table.
  val sauces = TableQuery[SauceTable]

  /** Retrieve the list of sauces */
  def list(): Future[Seq[Sauce]] = {
    val query = sauces.sortBy(_.name)
    db.run(query.result)
  }

  /** Retrieve a sauce from the id. */
  def findById(id: Long): Future[Option[Sauce]] =
    db.run(sauces.filter(_.id === id).result.headOption)

  /** Retrieve a sauce from the name. */
  def findByName(name: String): Future[Option[Sauce]] =
    db.run(sauces.filter(_.name === name).result.headOption)

  /** Insert a new sauce, then return it. */
  def insert(sauce: Sauce): Future[Sauce] = {
    val insertQuery = sauces returning sauces.map(_.id) into ((sauce, id) => sauce.copy(id))
    db.run(insertQuery += sauce)
  }

  /** Update a sauce, then return an integer that indicate if the sauce was found (1) or not (0). */
  def update(id: Long, sauce: Sauce): Future[Int] = {
    val sauceToUpdate: Sauce = sauce.copy(id)
    db.run(sauces.filter(_.id === id).update(sauceToUpdate))
  }

  /** Delete a sauce, then return an integer that indicate if the sauce was found (1) or not (0). */
  def delete(id: Long): Future[Int] =
    db.run(sauces.filter(_.id === id).delete)
}