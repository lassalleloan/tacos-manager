# Tacos Manager :: A Scala Play project

Authors: Loan Lassalle, Julien Brêchet and Adrien Marco
***

## Description
As part of the Scala course, the Tacos Manager project consists of creating a web application and its database in order to put into practice the knowledge acquired in class.
The application makes it possible to optimize the organization of the preparation of the menus for the customers announcing themselves via an interface of reservation. This concept is used to reduce waiting time on site during meal preparation.

Objectives of the application:
- Management of user accounts (clients) and an administrator account (restaurateur)
- Main page highlighting the different offers (anyone can consult it)
- A user can create a user account or log in via an account to place an order (only if logged in). An order consists of tacos, drinks and chips and can be made within 24 hours and not before. Meals are only served between 11am and 2pm and payment is made on site.
- An administrator (hard-coded account) can view all orders for the day and create, update and delete tacos, drinks and chips. An order specifies the time at which it must be ready and its contents as well as the customer who orders it. The user's contact details may be used to notify the user in the event of unforeseen circumstances (telephone number, e-mail address).
- Optional: addition of a management interface for the administrator (stock, new tacos, ...).

## Interface
The possible actions are separated into several pages, each having its role.

### Page d'accueil
This page is used to display a description of the application and, like all other pages, to provide a navigation menu to perform the various queries:

![home page](readme_images/home_page.png)

### Signup page
This page allows the user to register to order:

![signup page](readme_images/sign_up_page.png)

### Login page
This page allows the user to login:

![login page](readme_images/login_page.png)

### Page to display current orders
This page allows the user to view current orders:

![order page](readme_images/user_show_orders.png)

### Order page
This page allows the user to place an order:

![order page](readme_images/user_order.png)

### Order viewing page
This page, intended for the administrator, allows to consult the orders to prepare:

![order viewing page](readme_images/admin_show_orders.png)

## Implementation

The project is developed in Scala with the Slick library for database queries and the Play framework for the web aspect of the application.

### Database
This is the relational model of the database:

![relational model](readme_images/schema_relationnel.png)

An order is represented as the set of tacos selected in connection with fries, drinks and the person placing the order. Price and hour of service are also taken into account.

The sauces, meats and ingredients which constitute a taco are here for information purposes because the application does not allow the customer to personalize himself the constitution of a taco. He must choose among the existing compositions corresponding to the names of the tacos on the list.

### Routing
Here are the navigation possibilities offered by the application:

![routes](readme_images/routes.png)

Note: the other pages not mentioned are inaccessible and protected thanks to the Play framework. Of course, it was necessary to implement the notions of session and user role.

### Sessions and user roles

For example, here is how you can redirect simple users or the administrator to the appropriate pages after checking that the credentials match an existing account:

![checkConnection](readme_images/check_connetion.png)

Note: This is where you create a session for an authenticated user.

### Controllers

<ul>
<li>tacosHomeController: manages the entry point in the application by returning the view from the home page</li>
<li>tacosManagementController: supposed to provide a product management interface for the restorer but this optional aspect has not been implemented</li>
<li>tacosShowOrdersController: allows to get the list of the commands of the day and to display them on the corresponding view</li>
<li>tacosUserAdminConnectionController: manages sessions and roles of users/admin</li>
<li>tacosUserShowOrdersController: allows to obtain the list of the current orders made by the client and to display them on the corresponding view</li>
<li>tacosUserOrderController: manages the creation of a command</li>
<li>tacosUserSignUpController: manages the creation of new user accounts</li>
</ul>

### DAOs
After having created a model which represents the various objects to be manipulated, it is necessary to be able to establish a link between these and the values which the DAO will return during the requests made to the database.
If we take for example the CAD of the command handlers (OrderDAO), we must map the values as follows:

![DAO1](readme_images/order_dao_1.png)

Then, it is possible to create a query for the database and retrieve the answer. Here is how you can for example (Slick) get all orders made keeping only the items useful for the restaurateur (customer name, price, selected products, quantities, service time):

![DAO2](readme_images/order_dao_2.png)

Note: it is therefore necessary to have a DAO for each table of the relational schema to represent each entity.

## Conclusion
The application is functional, although there is still the management page to implement (optional point according to the specifications).

It should be noted that not all the functionalities offered by the DAO are used. These are available in the possibility of a possible improvement of the application.
