# Circle of Life Server
This project is the server of the Android-Application 
[CircleOfLife](https://github.com/Lezhor/circle_of_life). 
Both projects are written in Java.
The server communicates with the Client via Sockets and IO-Streams.
It also has a connection to a PostGre-DB. It accesses it via JDBC.

# Setup
If you want to setup the server, you should first configure the
postgre database and setup the connection-driver.

Use tools like [pgAdmin 4](https://www.pgadmin.org/download/) to setup a Postgre-Database.
The Database-Structure can be easily setup with the sql-scheme found in the following file:

```txt
src/main/resources/circle_of_life_db_scheme_backup.sql
```

After you setup the database you need to go to the file:

```txt
src/main/java/de/htw_berlin/database/jdbc/JDBCController.java
```

and set the following constants:

```java
public class JDBCController {
    public static final String JDBC_IP = "your_ip"; // Most probably "localhost"
    public static final String JDBC_DATABASE = "your_db_name";
    public static final String JDBC_USERNAME = "your_pgadmin_account";
    public static final String JDBC_PASSWORD = "your_pgadmin_password";
}
```

After you did this you can package the server to a jar file using:

```bash
mvn clean package
```

To start the server open the terminal and launch the jar-file:
```bash
java -jar .\target\CircleOfLifeServer-1.0.jar
```

Note that for this to work your machine needs to run a Java-Version of at least Java 19