package models

// Represent a database's course entry; the ID is optional because we don't necessary want to have it (for example when
// we create a new course).
case class Course(id: Option[Long], name: String, description: String, hasApero: Option[Boolean])

// Represent a database's student entry.
case class Student(id: Option[Long], firstName: String, lastName: String, age: Int, isInsolent: Boolean)

// Represent a database's course <- >student entry.
case class CourseStudent(id: Option[Long], courseId: Long, studentId: Long)

case class Person(id: Option[Long], name: String, firstName: String, phone: String, mail: String,
                  login: String, password: String, rolePerson: Long)

case class RolePerson(id: Option[Long], name: String)

case class Order(id: Option[Long], dateOrder: String, hourOrder: String, price: Double, person: Long)

case class Fry(id: Option[Long], name: String, portion: Int, price: Double)

case class OrderFry(orderId: Long, fryId: Long)

case class Drink(id: Option[Long], name: String, deciliter: Int, price: Double)

case class OrderDrink(ordeId: Long, drinkId: Long)

case class Tacos(id: Option[Long], name: String, price: Double)

case class OrderTacos(ordeId: Long, tacosId: Long)

case class Sauce(id: Option[Long], name: String, spicy: Int)

case class TacosSauce(tacosId: Long, sauceId: Long)

case class Meat(id: Option[Long], name: String, origin: String)

case class TacosMeat(tacosId: Long, meatId: Long)

case class Ingredient(id: Option[Long], name: String, origin: String)

case class TacosIngredient(tacosId: Long, ingredientId: Long)
