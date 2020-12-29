Oompa Loompa Spring Boot Project

includes sample usages for: Hystrix Circuit Breaker, Docker, Spring Redis Cache, Logging by Spring AOP, Central Exception Handler,
Liquibase, Mockito and some libraries.

Used Libraries: 

<b>Liquibase: </b>

Used for automatic database schema creation, updates on table structures and tracking changes in database schema. 
It provides easy switch between table versions and automatic updates on table structure.
By this way you can track table structure changes easily, similar to tracking code changes in git history. 

<b>Netflix-Hystrix:</b>
Enabling Circuit breaker in application. Used in OompaLoompa Controller to show an example, update and save buttons wrapped with Hystrix Command
as if it is risky code, in case of failure or timeout it will execute fallback method. 
The goal is to avoid waiting times for responses and thus treating every request as failed where no response was received within the timeout. 

For update operation, IllegalArgument and NotFound exceptions are excluded from HystrixCommand, as these exceptions are expected to occur, 
and handled by Exception Handler.
 
<b>Spring-cache:</b>

Used for caching service query calls, backed by <b>Redis.</b>

In this application, we Evict Cache on update method, as there is not  Delete operation in api, we evict Key from cache in 
all updates, so next findById call wont be using value from cache, as it is removed, after first call on findById, if there 
is not any updates before next call, it will use Cache.
   
For installing redis locally run :
docker run --name redis-cache -p 6379:6379 -d redis, or check docker-compose file. 

<b>Spring Aop:</b>

Used for creating generic logs, in LoggerAdvice, which logs on pointcuts informs about method execution.
Also created a new annotation "LogMe" in log package, LoggerAdvice watchs methods annotated with that annotation and logs 
annotation default input value before method call, also adds another line after execution completed.
Sample Usage: 
@LogMe("fetching data from db.."")

 
<b>Swagger:</b>
added as dependecy to be able to view api documentation, just go to /api-docs url, get generated json and paste into Swagger editor to see OpenApi Definitons.


<b>Logback:</b> For logging

<b>Mockito:</b> For mocking
<b>Hamcrest:</b> More readable assert statements like : assertThat(actual,equalTo(expected)) 
Tried to follow 3A pattern in test class. (Arrange, Act, Assert)

<b>Docker:</b> For creating app container.

There is a docker-compose.yml in the root directory of project,  it is not included in project files, i just put, it can be used for setting environment. 
just run :
docker-compose up -d

and it will pull and run required services, redis and mysql. It is creating a network named "napptilusnet", after running this, you can use docker plugin included
in pom.xml, it will create a container for this app in the same network. 

If you just want to set up environment for using redis and mysql locally, then just remove network sections from docker compose then run 
docker-compose up -d
