package dao

import scala.concurrent.Future
import javax.inject.{Inject, Singleton}
import models.OrderDrink
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

// We use a trait component here in order to share the OrderDrink class with other DAO, thanks to the inheritance.
trait OrderDrinkComponent extends OrderComponent with DrinkComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's "asso_commande_boisson" table in a object-oriented entity: the OrderDrink model.
  class OrderDrinkTable(tag: Tag) extends Table[OrderDrink](tag, "asso_commande_boisson") {
    def orderId = column[Long]("commande_pk_fk", O.PrimaryKey) // Primary key
    def drinkId = column[Long]("boisson_pk_fk", O.PrimaryKey) // Primary key
    def quantity = column[Int]("quantite")

    // Map the attributes with the model.
    def * = (orderId, drinkId, quantity) <> (OrderDrink.tupled, OrderDrink.unapply)
  }
}

// This class contains the object-oriented list of users and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the user query table and loads the JDBC profile configured in the application's
// configuration file.
