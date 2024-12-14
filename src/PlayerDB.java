import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDB {
    public static void addPlayer(String playerName, int initialPoints) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO players (name, points) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, playerName);
            statement.setInt(2, initialPoints);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getPlayerPoints(String playerName) {
        int points = 0;
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT points FROM players WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, playerName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                points = resultSet.getInt("points");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return points;
    }

    public static void updatePlayerPoints(String playerName, int points) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE players SET points = ? WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, points);
            statement.setString(2, playerName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
