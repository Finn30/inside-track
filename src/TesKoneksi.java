import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TesKoneksi {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/insidetrack";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected to the database successfully!");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database!");
            e.printStackTrace();
        }
    }
}