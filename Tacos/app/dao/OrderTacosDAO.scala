package dao

import scala.concurrent.Future
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
