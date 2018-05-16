package models

case class User(id: Long, firstName: String, lastName: String, phone: Option[String], email: String, password: String, userRole: Long)

case class RoleUser(id: Long, name: String)

case class Order(id: Long, dateOrder: Option[String], hourOrder: String, price: Double, person: Long)

case class Fry(id: Long, name: String, portion: Int, price: Double)

case class OrderFry(orderId: Long, fryId: Long, quantity: Int)

case class Drink(id: Long, name: String, deciliter: Int, price: Double)

case class OrderDrink(ordeId: Long, drinkId: Long)

case class Tacos(id: Option[Long], name: String, price: Double)

case class OrderTacos(ordeId: Long, tacosId: Long)

case class Sauce(id: Option[Long], name: String, spicy: Int)

case class TacosSauce(tacosId: Long, sauceId: Long)

case class Meat(id: Option[Long], name: String, origin: String)

case class TacosMeat(tacosId: Long, meatId: Long)

case class Ingredient(id: Option[Long], name: String, origin: String)

case class TacosIngredient(tacosId: Long, ingredientId: Long)