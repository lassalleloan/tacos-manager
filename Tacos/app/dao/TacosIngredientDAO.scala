package dao

import models.TacosIngredient
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

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
