package dao

import scala.concurrent.Future
import javax.inject.{Inject, Singleton}
import models.Meat
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

trait MeatComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's meat table in a object-oriented entity: the Meat model.
  class FryTable(tag: Tag) extends Table[Meat](tag, "viande") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // Primary key, auto-incremented
    def name = column[String]("nom")
    def origin = column[String]("provenance")

    // Map the attributes with the model.
    def * = (id, name, origin) <> (Meat.tupled, Meat.unapply)
  }
}

// This class contains the object-oriented list of users and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the user query table and loads the JDBC profile configured in the application's
// configuration file.
