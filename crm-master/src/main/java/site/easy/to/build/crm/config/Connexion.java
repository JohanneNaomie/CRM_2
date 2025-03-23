package site.easy.to.build.crm.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {

    private static final String URL = "jdbc:mysql://localhost:3306/crm"; // Change CRM to your actual DB name
    private static final String USER = "root"; // Change to your DB username
    private static final String PASSWORD = "password"; // Change to your DB password
    private static Connection connection;

    // Private constructor to prevent instantiation
    private Connexion() {}

    // Method to get a connection instance
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load MySQL JDBC Driver (optional, depends on version)
                Class.forName("com.mysql.cj.jdbc.Driver");
                // Establish connection
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Database connection established!");
            } catch (ClassNotFoundException e) {
                System.err.println("❌ MySQL JDBC Driver not found: " + e.getMessage());
                throw new SQLException(e);
            } catch (SQLException e) {
                System.err.println("❌ Error connecting to database: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }
}
