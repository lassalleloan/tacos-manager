package dao

import javax.inject.{Inject, Singleton}

import models.Ingredient
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

trait IngredientComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's "ingredient" table in a object-oriented entity: the Ingredient model.
  class IngredientTable(tag: Tag) extends Table[Ingredient](tag, "ingredient") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // Primary key, auto-incremented
    def name = column[String]("nom")
    def origin = column[String]("provenance")

    // Map the attributes with the model.
    def * = (id, name, origin) <> (Ingredient.tupled, Ingredient.unapply)
  }
}

// This class contains the object-oriented list of users and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the user query table and loads the JDBC profile configured in the application's
// configuration file.
@Singleton
class ingredientDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends IngredientComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  // Get the object-oriented list of ingredients directly from the query table.
  val ingredients = TableQuery[IngredientTable]

  /** Retrieve the list of ingredients sorted by name */
  def list(): Future[Seq[Ingredient]] = {
    val query = ingredients.sortBy(_.name)
    db.run(query.result)
  }

  /** Retrieve an ingredient from the id. */
  def findById(id: Long): Future[Option[Ingredient]] =
    db.run(ingredients.filter(_.id === id).result.headOption)

  /** Retrieve an ingredient from the name. */
  def findByName(name: String): Future[Option[Ingredient]] =
    db.run(ingredients.filter(_.name === name).result.headOption)

  /** Insert a new ingredient, then return it. */
  def insert(ingredient: Ingredient): Future[Ingredient] = {
    val insertQuery = ingredients returning ingredients.map(_.id) into ((ingredient, id) => ingredient.copy(id))
    db.run(insertQuery += ingredient)
  }

  /** Update an ingredient, then return an integer that indicate if the ingredient was found (1) or not (0). */
  def update(id: Long, ingredient: Ingredient): Future[Int] = {
    val ingredientToUpdate: Ingredient = ingredient.copy(id)
    db.run(ingredients.filter(_.id === id).update(ingredientToUpdate))
  }

  /** Delete an ingredient, then return an integer that indicate if the ingredient was found (1) or not (0). */
  def delete(id: Long): Future[Int] =
    db.run(ingredients.filter(_.id === id).delete)
}