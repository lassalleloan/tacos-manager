package dao

import models.Order
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

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
    def * = (id, dateOrder.?, hourOrder, price, user) <> (Order.tupled, Order.unapply)
  }
}

// This class contains the object-oriented list of users and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the user query table and loads the JDBC profile configured in the application's
// configuration file.
