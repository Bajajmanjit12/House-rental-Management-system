import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropertyDAO {
    // Table formatting constants
    private static final String TABLE_HEADER =
            "+-------+----------------------+-----------------+------------+-------------+------------+\n" +
                    "| ID    | Address              | City            | Type       | Rent        | Status     |\n" +
                    "+-------+----------------------+-----------------+------------+-------------+------------+";

    private static final String TABLE_FOOTER =
            "+-------+----------------------+-----------------+------------+-------------+------------+";

    private static final String TABLE_ROW_FORMAT =
            "| %-5d | %-20s | %-15s | %-10s | $%-10.2f | %-10s |%n";

    // Add a new property
    public static boolean addProperty(Property property) {
        String sql = "INSERT INTO Properties (address, city, type, monthly_rent, availability_status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, truncateString(property.getAddress(), 50));
            pstmt.setString(2, truncateString(property.getCity(), 15));
            pstmt.setString(3, truncateString(property.getType(), 10));
            pstmt.setDouble(4, property.getMonthlyRent());
            pstmt.setString(5, truncateString(property.getAvailabilityStatus(), 10));

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding property: " + e.getMessage());
            return false;
        }
    }

    // Get all properties
    public static List<Property> getAllProperties() {
        List<Property> properties = new ArrayList<>();
        String sql = "SELECT * FROM Properties";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                properties.add(new Property(
                        rs.getInt("property_id"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("type"),
                        rs.getDouble("monthly_rent"),
                        rs.getString("availability_status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching properties: " + e.getMessage());
        }
        return properties;
    }

    // Display all properties in table format
    public static void displayAllProperties() {
        displayPropertyList(getAllProperties(), "PROPERTY LISTING");
    }

    // Search methods
    public static void displayPropertiesByType(String type) {
        String sql = "SELECT * FROM Properties WHERE LOWER(type) LIKE LOWER(?)";
        displayPropertyList(searchProperties(sql, "%" + type + "%"), "PROPERTIES OF TYPE: " + type);
    }

    public static void displayPropertiesByCity(String city) {
        String sql = "SELECT * FROM Properties WHERE LOWER(city) LIKE LOWER(?)";
        displayPropertyList(searchProperties(sql, "%" + city + "%"), "PROPERTIES IN CITY: " + city);
    }

    public static void displayPropertiesByPriceRange(double min, double max) {
        String sql = "SELECT * FROM Properties WHERE monthly_rent BETWEEN ? AND ?";
        List<Property> properties = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, min);
            pstmt.setDouble(2, max);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                properties.add(new Property(
                        rs.getInt("property_id"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("type"),
                        rs.getDouble("monthly_rent"),
                        rs.getString("availability_status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error searching by price: " + e.getMessage());
        }

        displayPropertyList(properties, String.format("PROPERTIES PRICED $%.2f - $%.2f", min, max));
    }

    // Helper methods
    private static List<Property> searchProperties(String sql, String param) {
        List<Property> properties = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, param);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                properties.add(new Property(
                        rs.getInt("property_id"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("type"),
                        rs.getDouble("monthly_rent"),
                        rs.getString("availability_status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error in search: " + e.getMessage());
        }
        return properties;
    }

    private static void displayPropertyList(List<Property> properties, String title) {
        System.out.println("\n" + title);
        System.out.println(TABLE_HEADER);

        if (properties.isEmpty()) {
            System.out.printf("| %-83s |%n", "No properties found");
        } else {
            for (Property p : properties) {
                System.out.printf(TABLE_ROW_FORMAT,
                        p.getPropertyId(),
                        truncateString(p.getAddress(), 20),
                        truncateString(p.getCity(), 15),
                        truncateString(p.getType(), 10),
                        p.getMonthlyRent(),
                        truncateString(p.getAvailabilityStatus(), 10));
            }
        }
        System.out.println(TABLE_FOOTER);
    }

    private static String truncateString(String str, int maxLength) {
        if (str == null) return "";
        return str.length() > maxLength ? str.substring(0, maxLength - 3) + "..." : str;
    }
}