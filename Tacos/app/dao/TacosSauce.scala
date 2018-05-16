package dao

import scala.concurrent.Future
import javax.inject.{Inject, Singleton}
import models.TacosSauce
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

// We use a trait component here in order to share the TacosSauce class with other DAO, thanks to the inheritance.
trait TacosSauceComponent extends SauceComponent with TacosComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's "asso_sauce_tacos" table in a object-oriented entity: the TacosSauce model.
  class TacosSauceTable(tag: Tag) extends Table[TacosSauce](tag, "asso_sauce_tacos") {
    def tacosId = column[Long]("tacos_pk_fk", O.PrimaryKey) // Primary key
    def sauceId = column[Long]("sauce_pk_fk", O.PrimaryKey) // Primary key

    // Map the attributes with the model.
    def * = (tacosId, sauceId) <> (TacosSauce.tupled, TacosSauce.unapply)
  }
}

// This class contains the object-oriented list of users and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the user query table and loads the JDBC profile configured in the application's
// configuration file.
