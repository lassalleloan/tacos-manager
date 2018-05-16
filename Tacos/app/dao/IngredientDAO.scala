package dao

import models.Ingredient
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

trait IngredientComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's "ingredient" table in a object-oriented entity: the Ingredient model.
  class ingredientTable(tag: Tag) extends Table[Ingredient](tag, "ingredient") {
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
