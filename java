package cs623_project;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CS623ProjectT2 {

    public static void main(String[] args) throws SQLException, IOException {

        System.out.println("The product p1 is deleted from Product and Stock.");

        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5433/postgres", "postgres", "postgres");

        // For atomicity
        conn.setAutoCommit(false);

        // For isolation
        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

        Statement stmt = null;
        try {
            // Create statement object
            stmt = conn.createStatement();

            // Drop existing tables
            stmt.executeUpdate("DROP TABLE IF EXISTS Stock;");
            stmt.executeUpdate("DROP TABLE IF EXISTS Product;");

            // Recreate tables
            stmt.executeUpdate("CREATE TABLE Product(prod varchar(20) primary key, pname varchar(50), price float not null);");
            stmt.executeUpdate("CREATE TABLE Stock(prod varchar(20), dep varchar(20), quantity int not null);");

            // Delete product p1 from Stock and Product
            stmt.executeUpdate("DELETE FROM Stock WHERE prod = 'p1';");
            stmt.executeUpdate("DELETE FROM Product WHERE prod = 'p1';");

        } catch (SQLException e) {
            System.out.println("An exception was thrown");
            e.printStackTrace();
            // For atomicity
            conn.rollback();
            stmt.close();
            conn.close();
            return;
        }
        System.out.println("Deleted The product p1 from Product and Stock Successfully.");

        // Commit changes
        conn.commit();
        stmt.close();
        conn.close();
    }
}
