package dao

import javax.inject.{Inject, Singleton}

import models.OrderTacos
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

// We use a trait component here in order to share the OrderTacos class with other DAO, thanks to the inheritance.
trait OrderTacosComponent extends OrderComponent with TacosComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's "asso_commande_tacos" table in a object-oriented entity: the OrderTacos model.
  class OrderTacosTable(tag: Tag) extends Table[OrderTacos](tag, "asso_commande_tacos") {
    def orderId = column[Long]("commande_pk_fk", O.PrimaryKey) // Primary key
    def tacosId = column[Long]("tacos_pk_fk", O.PrimaryKey) // Primary key
    def quantity = column[Int]("quantite")

    // Map the attributes with the model.
    def * = (orderId, tacosId, quantity) <> (OrderTacos.tupled, OrderTacos.unapply)
  }
}

// This class contains the object-oriented list of users and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the user query table and loads the JDBC profile configured in the application's
// configuration file.
@Singleton
class OrderTacosDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends OrderTacosComponent with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  // Get the object-oriented list of orderTacos directly from the query table.
  val orderTacos = TableQuery[OrderTacosTable]

  /** Retrieve the list of orderTacos sorted by orderId and tacosId */
  def list(): Future[Seq[OrderTacos]] = {
    val query = orderTacos.sortBy(x => (x.orderId, x.tacosId))
    db.run(query.result)
  }

  /** Retrieve a list of orderTacos from the orderId and the tacosId. */
  def findById(orderId: Long, tacosId: Long): Future[Option[OrderTacos]] =
    db.run(orderTacos.filter(_.orderId === orderId).filter(_.tacosId === tacosId).result.headOption)
}