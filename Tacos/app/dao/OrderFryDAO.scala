package dao

import javax.inject.{Inject, Singleton}

import models.OrderFry
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

// We use a trait component here in order to share the OrderFry class with other DAO, thanks to the inheritance.
trait OrderFryComponent extends OrderComponent with FryComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's "asso_commande_frite" table in a object-oriented entity: the OrderFry model.
  class OrderFryTable(tag: Tag) extends Table[OrderFry](tag, "asso_commande_frite") {
    def orderId = column[Long]("commande_pk_fk", O.PrimaryKey) // Primary key
    def fryId = column[Long]("frite_pk_fk", O.PrimaryKey) // Primary key
    def quantity = column[Int]("quantite")

    // Map the attributes with the model.
    def * = (orderId, fryId, quantity) <> (OrderFry.tupled, OrderFry.unapply)
  }
}

// This class contains the object-oriented list of users and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the user query table and loads the JDBC profile configured in the application's
// configuration file.
@Singleton
class OrderFryDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends OrderFryComponent with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  // Get the object-oriented list of orderFries directly from the query table.
  val orderFries = TableQuery[OrderFryTable]

  /** Retrieve the list of orderFries sorted by orderId and fryId */
  def list(): Future[Seq[OrderFry]] = {
    val query = orderFries.sortBy(x => (x.orderId, x.fryId))
    db.run(query.result)
  }

  /** Retrieve a list of orderFries from the orderId and the fryId. */
  def findById(orderId: Long, fryId: Long): Future[Option[OrderFry]] =
    db.run(orderFries.filter(_.orderId === orderId).filter(_.fryId === fryId).result.headOption)

  /** Retrieve a list of orderFries from the orderId. */
  def findByOrderId(orderId: Long): Future[Option[OrderFry]] =
    db.run(orderFries.filter(_.orderId === orderId).result.headOption)

  /** Insert a new orderFry, then return it. */
  def insert(orderFry: OrderFry): Future[OrderFry] = {
    // val insertQuery = orderFries returning orderFries.map(x => (x.orderId, x.fryId)) into ((orderFry, doubleId) => orderFry.copy(doubleId._1, doubleId._2))
    val insertQuery = orderFries returning orderFries.map(x => (x.orderId, x.fryId)) into ((orderFry, doubleId) => orderFry.copy(orderId = doubleId._1, fryId = doubleId._2))
    db.run(insertQuery += OrderFry(orderFry.orderId, orderFry.fryId, orderFry.quantity))
  }
}