package dao

import javax.inject.{Inject, Singleton}

import models.TacosIngredient
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

// We use a trait component here in order to share the TacosIngredient class with other DAO, thanks to the inheritance.
trait TacosIngredientComponent extends IngredientComponent with TacosComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's "asso_ingredient_tacos" table in a object-oriented entity: the TacosIngredient model.
  class TacosIngredientTable(tag: Tag) extends Table[TacosIngredient](tag, "asso_ingredient_tacos") {
    def tacosId = column[Long]("tacos_pk_fk", O.PrimaryKey) // Primary key
    def ingredientId = column[Long]("ingredient_pk_fk", O.PrimaryKey) // Primary key

    // Map the attributes with the model.
    def * = (tacosId, ingredientId) <> (TacosIngredient.tupled, TacosIngredient.unapply)
  }
}

// This class contains the object-oriented list of users and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the user query table and loads the JDBC profile configured in the application's
// configuration file.
@Singleton
class TacosIngredientDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends TacosIngredientComponent with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  // Get the object-oriented list of tacosIngredients directly from the query table.
  val tacosIngredients = TableQuery[TacosIngredientTable]

  /** Retrieve the list of tacosIngredients sorted by tacosId and ingredientId */
  def list(): Future[Seq[TacosIngredient]] = {
    val query = tacosIngredients.sortBy(x => (x.tacosId, x.ingredientId))
    db.run(query.result)
  }

  /** Retrieve a list of tacosIngredients from the tacosId and the ingredientId. */
  def findById(tacosId: Long, ingredientId: Long): Future[Option[TacosIngredient]] =
    db.run(tacosIngredients.filter(_.tacosId === tacosId).filter(_.ingredientId === ingredientId).result.headOption)

  /** Retrieve a list of tacosIngredients from the tacosId. */
  def findByTacosId(tacosId: Long): Future[Option[TacosIngredient]] =
    db.run(tacosIngredients.filter(_.tacosId === tacosId).result.headOption)
}