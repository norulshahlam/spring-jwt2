# Spring Security with JWT - Version 6 - Code refectoring

There are many repeeated codes so let's refactor them!
Also lets extract JWT variables into property variables for security


# Spring Security with JWT - Version 5 - Add more URL requests

All this while we only have getAllUser() and refresh token in our controller. Lets add more requests handler!
 
`Get 1 user`

    GET http://localhost:8081/api/users/john

`Create user`

    POST http://localhost:8081/api/user/save
    {
        "name": "John Doe",
        "username": "john",
        "password": "abcd"
    }

`Create role`

    POST http://localhost:8081/api/role/save
    {
        "name": "ROLE_MASTER"
    }

`Add role to a user`

    POST http://localhost:8081/api/role/addtouser
    {
        "username":"john",
        "roleName":"ROLE_MANAGER"
    }



### Test

Get 1 user:


# Spring Security with JWT - Version 4 - Refresh JWT token

During successful login, an access token and refresh token is generated. Access token usually have short expiry so when we attempt to make a request later it will fail. To overcome this, we have our refresh token which has longer expiry. We will use this to generate a new set of access and refresh token, similar to when we /login

### High level overview

- Create controller for /token/refresh. Here we will check JWT, then create new access & refresh token.
- In security filter, allow this url to pass thru

### Test

- Set token to expire in 1 min to simulate expired token
- Login to get token
- GET localhost:8081/api/users with your token to see if your access token is working
- Wait for a minute to expire your access token
- Get all users again to check if it expires
- If it has expires, GET localhost:8081/api/token/refresh/ with your refresh token
- You will get a new set of access and refresh token
- GET localhost:8081/api/users with your NEW access token to see if your NEW access token is working


# Spring Security with JWT - Version 3 - Read JWT

### CustomAuthorizationFilter

- Now that we have created JWT in our CustomAuthenticationFilter, lets create CustomAuthorizationFilter to read JWT

- Add this class as argument in addFilterBefore() so it will filter all requests. This ensures users must be authenticated before making any requests.

- Since this is to check whether user is authenticated, exclude requests for login and refresh tokens.


## Testing

Run the same step to generate jwt

    POST http:8081/login

    > Body > x-www-form >

    username: john
    password: 1234

When you get the JWT, make another request again:

    GET localhost:8081/api/users
    > Headers >  Authorisation: Bearer <token>

The response will return list of users


# Spring Security with JWT - Version 2 - Generate JWT 

## High level overview

- Encrypt your user's password using BCrypt
- Setup SecurityConfig
- Implement custom UserDetailsService to validate user credentials
- Create a CustomAuthenticationFilter to intercept only for logging in. Here is where we generate JWT. 
- Inside CustomAuthenticationFilter, get login details and use our custom UserDetailsService to validate
- If validation success, generate JWT
- Set your session to stateless


## Testing

- Lets test to see if we can generate JWT  

Use Postman:  

        POST http:8081/login

        > Body > x-www-form >

        username: john
        password: 1234

You will get jwt as the response.



###   
# Spring Security with JWT - Version 1 - Setup User and roles and secure API  
###   


There will be in 4 parts in this tutorial:  
- Version 1. Setup User and roles and secure API
- Version 2. Generate jwt token
- Version 3. Verify jwt token
- Version 4. Refresh token

### Use Case

Access all users using 'user' as user and generated password as passowrd

### Structure  

A user will have basic details like name, username, id, password.  
A user can >1 roles.

        
    +----+-----------------------+----------+----------+
    | id | name                  | password | username |
    +----+-----------------------+----------+----------+
    |  5 | John Travolta         | 1234     | john     |
    |  6 | Will Smith            | 1234     | will     |
    |  7 | Jim Carry             | 1234     | jim      |
    |  8 | Arnold Schwarzenegger | 1234     | arnold   |
    +----+-----------------------+----------+----------+


    +----+------------------+
    | id | name             |
    +----+------------------+
    |  1 | ROLE_USER        |
    |  2 | ROLE_MANAGER     |
    |  3 | ROLE_ADMIN       |
    |  4 | ROLE_SUPER_ADMIN |
    +----+------------------+

        
    +---------+----------+
    | user_id | roles_id |
    +---------+----------+
    |       5 |        1 |
    |       6 |        2 |
    |       7 |        3 |
    |       8 |        4 |
    |       8 |        3 |
    |       8 |        1 |
    +---------+----------+

The ouput will be something like this:

    {
    "id": 8,
    "name": "Arnold Schwarzenegger",
    "username": "arnold",
    "password": "1234",
    "roles": [
      {
        "id": 4,
        "name": "ROLE_SUPER_ADMIN"
      },
      {
        "id": 3,
        "name": "ROLE_ADMIN"
      },
      {
        "id": 1,
        "name": "ROLE_USER"
      }
    ]
  }




### Setup docker for db

    docker run --detach --env MYSQL_ROOT_PASSWORD=root --env MYSQL_DATABASE=mydb --env MYSQL_PASSWORD=root --env MYSQL_USER=admin --name localhost --publish 3306:3306 mysql:8.0

### Create project with dependencies

    web
    jpa
    mysql connector
    commons-lang
    lombok
    jaxb-api
    security
    java-jwt

### Configure datasource

Always confirm your connection with db before anything else. Once conencted, then you can setup the rest.  

    spring.datasource.url=jdbc:mysql://localhost:3306/mydb?useLegacyDatetimeCode=false&serverTimezone=UTC
    spring.datasource.username=admin
    spring.datasource.password=root
    server.port=9091
        
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
    spring.jpa.properties.hibernate.format.sql=true
    spring.jpa.hibernate.ddl-auto=create
    spring.sql.init.mode=always

Once datasource is added, run the app to see if it manages to connect. And also check if database is created:  

### Test DB

Run docker mysql in cli  

    docker exec -it localhost bash  

Connect to mysql   

    mysql -u admin -proot  

Test   

    use mydb;  
    show databases;

### Entity, Repo, Service, Controller.

- Define your User & Role entity 
- Make sure User has ManyToMany relationship with Role 
- UserService must contain methods to add user, role, and add role to user.

Check your DB again to see if those tables are created accordingly:  

    use mydb;  
    show tables;  
    select * from user;  
    select * from role;  
    select * from user_roles;  

### Add schema & data in db on Spring start

- Use CommandLineRunner to add initial data into db
- Or use data.sql. Either way.
- Now check DB and see if all data are added sucessfully.


### Configure servlet context

    server.servlet.context-path=/jwt

### Test initial data in browser /api/getUsers

- On any BROWSER, go to: localhost:8081/api/users

Since we use spring security dependency, it will secure by default. on runtime, it will generate default password.  use 'user' as your default username. if success, u will be able to see all users

### Test initial data in db

Run mysql in cli using docker  

    docker exec -it localhost bash  

Connect to mysql   

    mysql -u admin -proot  

Test   

    use mydb;  
    show tables;  
    select * from user;  
    select * from role;  
    select * from user_roles;  

Stop & remove all running proceses  

    docker rm $(docker ps -a -q) -f  






