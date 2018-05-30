package dao

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
@Singleton
class OrderDrinkDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends OrderDrinkComponent with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  // Get the object-oriented list of orderDrinks directly from the query table.
  val orderDrinks = TableQuery[OrderDrinkTable]

  /** Retrieve the list of orderDrinks sorted by orderId and drinkId */
  def list(): Future[Seq[OrderDrink]] = {
    val query = orderDrinks.sortBy(x => (x.orderId, x.drinkId))
    db.run(query.result)
  }

  /** Retrieve a list of orderDrinks from the orderId and the drinkId. */
  def findById(orderId: Long, drinkId: Long): Future[Option[OrderDrink]] =
    db.run(orderDrinks.filter(_.orderId === orderId).filter(_.drinkId === drinkId).result.headOption)

  /** Retrieve a list of orderDrinks from the orderId. */
  def findByOrderId(orderId: Long): Future[Option[OrderDrink]] =
    db.run(orderDrinks.filter(_.orderId === orderId).result.headOption)

  /** Insert a new orderDrink, then return it. */
  def insert(orderDrink: OrderDrink): Future[OrderDrink] = {
    val insertQuery = orderDrinks returning orderDrinks.map(x => (x.orderId, x.drinkId)) into ((orderDrink, doubleId) => orderDrink.copy(doubleId._1, doubleId._2))
    db.run(insertQuery += orderDrink)
  }
}