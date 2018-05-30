package models

case class User(id: Option[Long], firstName: String, lastName: String, phone: Option[String], email: String, password: String, roleUser: Long)

case class RoleUser(id: Long, name: String)

case class Order(id: Option[Long], dateOrder: Option[String], hourOrder: String, price: Double, person: Long)

case class Fry(id: Long, name: String, portion: Int, price: Double)

case class OrderFry(orderId: Long, fryId: Long, quantity: Int)

case class Drink(id: Long, name: String, deciliter: Int, price: Double)

case class OrderDrink(orderId: Long, drinkId: Long, quantity: Int)

case class Tacos(id: Long, name: String, price: Double)

case class OrderTacos(orderId: Long, tacosId: Long, quantity: Int)

case class Sauce(id: Long, name: String, spicy: Int)

case class TacosSauce(tacosId: Long, sauceId: Long)

case class Meat(id: Long, name: String, origin: String)

case class TacosMeat(tacosId: Long, meatId: Long)

case class Ingredient(id: Long, name: String, origin: String)

case class TacosIngredient(tacosId: Long, ingredientId: Long)