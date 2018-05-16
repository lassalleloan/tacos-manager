package dao

import models.OrderFry
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

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
