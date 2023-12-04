package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DbConnector {
    private Connection connection = null;
    private Statement statement = null;

    public void connectToDB() {
        try {
            Properties properties = new Properties();
            properties.setProperty("user", "root");
            properties.setProperty("password", "12345678");
            properties.setProperty("useSSL", "false");
            //Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/university?useSSL=true&allowPublicKeyRetrieval=true", properties);
            this.statement = connection.createStatement();
        } catch (SQLException e ) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }
}
