package dao

import javax.inject.{Inject, Singleton}

import models.{Order, User}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

// We use a trait component here in order to share the Order class with other DAO, thanks to the inheritance.
trait OrderComponent extends UserComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's "commande" table in a object-oriented entity: the Order model.
  class OrderTable(tag: Tag) extends Table[Order](tag, "commande") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // Primary key, auto-incremented
    def dateOrder = column[String]("dateCommande")
    def hourOrder = column[String]("heureCommande")
    def price = column[Double]("prix")
    def user = column[Long]("personne_fk")

    // Map the attributes with the model.
    def * = (id.?, dateOrder.?, hourOrder, price, user) <> (Order.tupled, Order.unapply)
  }
}

// This class contains the object-oriented list of users and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the user query table and loads the JDBC profile configured in the application's
// configuration file.
@Singleton
class OrderDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends OrderComponent with UserComponent with RoleUserComponent with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  // Get the object-oriented list of orders directly from the query table.
  val orders = TableQuery[OrderTable]
  val users = TableQuery[UserTable]

  /** Retrieve the list of orders sorted by date and hour */
  def list(): Future[Seq[Order]] = {
    val query = orders.sortBy(x => (x.dateOrder, x.hourOrder))
    db.run(query.result)
  }

  /** Retrieve the list of orders sorted by date and hour */
  def listWithUsers(): Future[Seq[(Order, String)]] = {
    val query = orders.sortBy(x => (x.dateOrder, x.hourOrder))
      .join(users).on(_.user === _.id)
    db.run(query.result).map { ordersUsers =>
      for {
        (order, user) <- ordersUsers
      } yield (order, user.firstName.concat(" ").concat(user.lastName))
    }
  }

  /** Retrieve the list of orders for a specific day sorted by the hour of the order */
  def list(day: String): Future[Seq[Order]] = {
    val query = orders.filter(_.dateOrder === day).sortBy(o => o.hourOrder)
    db.run(query.result)
  }

  /** Retrieve an order from the id. */
  def findById(id: Long): Future[Option[Order]] =
    db.run(orders.filter(_.id === id).result.headOption)
<<<<<<< HEAD
  }


=======

  /** Retrieve an order from the id of a user for a specific day. */
  def findByIdUserPerDay(id: Long, day: String): Future[Seq[Order]] =
    db.run(orders.filter(_.user === id).filter(_.dateOrder === day).sortBy(o => o.hourOrder).result)

  /** Retrieve an order from the id of a user for a specific day and since a specific hour. */
  def findByIdUserPerDay(id: Long, day: String, hour: String): Future[Seq[Order]] =
    db.run(orders.filter(_.user === id).filter(_.dateOrder === day).filter(_.hourOrder >= hour)
      .sortBy(o => o.hourOrder).result)

  /** Insert a new order, then return it. */
  def insert(order: Order): Future[Order] = {
    val insertQuery = orders returning orders.map(_.id) into ((order, id) => order.copy(Some(id)))
    db.run(insertQuery += order)
  }
}
>>>>>>> 161d059e9920b46023f362077428e1e8c31d0a67
