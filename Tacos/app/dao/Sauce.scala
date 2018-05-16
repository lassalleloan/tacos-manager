package dao

import models.Sauce
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

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
