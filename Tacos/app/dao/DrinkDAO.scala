package dao

import javax.inject.{Inject, Singleton}
import models.Drink
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

trait DrinkComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's "boisson" table in a object-oriented entity: the Drink model.
  class DrinkTable(tag: Tag) extends Table[Drink](tag, "boisson") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // Primary key, auto-incremented
    def name = column[String]("nom")
    def deciliter = column[Int]("decilitres")
    def price = column[Double]("prix")

    // Map the attributes with the model.
    def * = (id.?, name, deciliter, price) <> (Drink.tupled, Drink.unapply)
  }
}

// This class contains the object-oriented list of drinks and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the drink query table and loads the JDBC profile configured in the application's
// configuration file.
@Singleton
class DrinkDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends DrinkComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  // Get the object-oriented list of drinks directly from the query table.
  val drinks = TableQuery[DrinkTable]

  /** Retrieve the list of drinks sorted by name */
  def list(): Future[Seq[Drink]] = {
    val query = drinks.sortBy(_.name)
    db.run(query.result)
  }

  /** Retrieve a drink from the id. */
  def findById(id: Long): Future[Option[Drink]] =
    db.run(drinks.filter(_.id === id).result.headOption)

  /** Retrieve a drink from the name. */
  def findByName(name: String): Future[Option[Drink]] =
    db.run(drinks.filter(_.name === name).result.headOption)

  /** Insert a new drink, then return it. */
  def insert(drink: Drink): Future[Drink] = {
    val insertQuery = drinks returning drinks.map(_.id) into ((drink, id) => drink.copy(Some(id)))
    db.run(insertQuery += drink)
  }

  /** Update a drink, then return an integer that indicate if the drink was found (1) or not (0). */
  def update(id: Long, drink: Drink): Future[Int] = {
    val drinkToUpdate: Drink = drink.copy(Some(id))
    db.run(drinks.filter(_.id === id).update(drinkToUpdate))
  }

  /** Delete a drink, then return an integer that indicate if the drink was found (1) or not (0). */
  def delete(id: Long): Future[Int] =
    db.run(drinks.filter(_.id === id).delete)
}
