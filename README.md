# demo-jdbc

# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.8-SNAPSHOT/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.8-SNAPSHOT/maven-plugin/reference/html/#build-image)
* [JDBC API](https://docs.spring.io/spring-boot/docs/2.7.8-SNAPSHOT/reference/htmlsingle/#data.sql)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/2.7.8-SNAPSHOT/reference/htmlsingle/#appendix.configuration-metadata.annotation-processor)
* [Spring Data JDBC](https://docs.spring.io/spring-boot/docs/2.7.8-SNAPSHOT/reference/htmlsingle/#data.sql.jdbc)

### Guides

The following guides illustrate how to use some features concretely:

* [Accessing Relational Data using JDBC with Spring](https://spring.io/guides/gs/relational-data-access/)
* [Managing Transactions](https://spring.io/guides/gs/managing-transactions/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Using Spring Data JDBC](https://github.com/spring-projects/spring-data-examples/tree/master/jdbc/basics)

### How use?

The current example uses the MySQL database. Access to MySql Server is required for successful launch. application.yml contains database access settings. Change them as needed.

For examples, see DemoJdbcApplicationTests.
Before starting the examples, it is necessary to execute src/main/resources/schema.sql on the database server, this script will create the necessary database and database objects.

For the correct run of the examples, it is worth rebuilding the database for each test (execute schema.sql).

It is also possible to use H2 as a database. To do this, you should change pom.xml, comment next block

    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>

and uncomment

    <!-- <dependency>-->
    <!--    <groupId>com.h2database</groupId>-->
    <!--    <artifactId>h2</artifactId>-->
    <!--    <scope>runtime</scope>-->
    <!-- </dependency>-->

Next, modify application.yml and replace the content of schema.sql with the content of h2_schema.sql.

That's all there is to do.

For H2, the database will be automatically rebuilt on each run.
