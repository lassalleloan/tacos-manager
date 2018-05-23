package dao

import javax.inject.{Inject, Singleton}

import models.TacosMeat
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

// We use a trait component here in order to share the TacosMeat class with other DAO, thanks to the inheritance.
trait TacosMeatComponent extends MeatComponent with TacosComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's "asso_viande_tacos" table in a object-oriented entity: the TacosMeat model.
  class TacosMeatTable(tag: Tag) extends Table[TacosMeat](tag, "asso_viande_tacos") {
    def tacosId = column[Long]("tacos_pk_fk", O.PrimaryKey) // Primary key
    def meatId = column[Long]("viande_pk_fk", O.PrimaryKey) // Primary key

    // Map the attributes with the model.
    def * = (tacosId, meatId) <> (TacosMeat.tupled, TacosMeat.unapply)
  }
}

// This class contains the object-oriented list of users and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the user query table and loads the JDBC profile configured in the application's
// configuration file.
@Singleton
class TacosMeatDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends TacosMeatComponent with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  // Get the object-oriented list of tacosMeats directly from the query table.
  val tacosMeats = TableQuery[TacosMeatTable]

  /** Retrieve the list of tacosMeats sorted by tacosId and meatId */
  def list(): Future[Seq[TacosMeat]] = {
    val query = tacosMeats.sortBy(x => (x.tacosId, x.meatId))
    db.run(query.result)
  }

  /** Retrieve a list of tacosMeats from the tacosId and the meatId. */
  def findById(tacosId: Long, meatId: Long): Future[Option[TacosMeat]] =
    db.run(tacosMeats.filter(_.tacosId === tacosId).filter(_.meatId === meatId).result.headOption)

  /** Retrieve a list of tacosMeats from the tacosId. */
  def findByTacosId(tacosId: Long): Future[Option[TacosMeat]] =
    db.run(tacosMeats.filter(_.tacosId === tacosId).result.headOption)
}