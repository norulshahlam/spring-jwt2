# Spring Security with JWT - Version 3 - Read JWT

### CustomAuthorizationFilter

- Now that we have created JWT in our CustomAuthenticationFilter, lets create CustomAuthorizationFilter to read JWT

- Add this class as argument in addFilterBefore() so it will filter all requests. This ensures users must be authenticated before making any requests.

- Since this is to check whether user is authenticated, exclude requests for login and refresh tokens.


# Spring Security with JWT - Version 2 - Generate JWT 

## High level overview

- Encrypt your user's password using BCrypt
- Setup SecurityConfig
- Implement custom UserDetailsService to validate user credentials
- Create a CustomAuthenticationFilter to intercept only for logging in. Here is where we generate JWT. 
- Inside CustomAuthenticationFilter, get login details and use our custom UserDetailsService to validate
- If validation success, generate JWT
- Set your session to stateless




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






