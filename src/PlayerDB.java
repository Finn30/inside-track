import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDB {

    public static int getPlayerPoints(String username) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT points FROM players WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("points");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void updatePlayerPoints(String username, int points) {
        if (points < 0) {
            points = 0; // Pastikan poin tidak negatif
        }
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE players SET points = ? WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, points);
            statement.setString(2, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method untuk login
    public static boolean login(String username, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM players WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Jika ada hasil, login berhasil
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method untuk registrasi
    public static boolean register(String username, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO players (username, password) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
