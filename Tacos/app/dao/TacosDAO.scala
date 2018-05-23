package dao

import javax.inject.{Inject, Singleton}

import models.Tacos
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

trait TacosComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's "tacos" table in a object-oriented entity: the Tacos model.
  class TacosTable(tag: Tag) extends Table[Tacos](tag, "tacos") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // Primary key, auto-incremented
    def name = column[String]("nom")
    def price = column[Double]("prix")

    // Map the attributes with the model.
    def * = (id, name, price) <> (Tacos.tupled, Tacos.unapply)
  }
}

// This class contains the object-oriented list of users and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the user query table and loads the JDBC profile configured in the application's
// configuration file.
@Singleton
class TacosDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends TacosComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  // Get the object-oriented list of tacos directly from the query table.
  val tacos = TableQuery[TacosTable]

  /** Retrieve the list of tacos sorted by name */
  def list(): Future[Seq[Tacos]] = {
    val query = tacos.sortBy(x => x.name)
    db.run(query.result)
  }

  /** Retrieve a tacos from the id. */
  def findById(id: Long): Future[Option[Tacos]] =
    db.run(tacos.filter(_.id === id).result.headOption)
}