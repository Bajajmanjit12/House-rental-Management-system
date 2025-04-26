import java.util.Scanner;

public class MainMenu {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== House Rental Management System ===");
        showMainMenu();
    }

    private static void showMainMenu() {
        System.out.println("\nMAIN MENU");
        System.out.println("1. Manage Properties");
        System.out.println("2. Manage Tenants");
        System.out.println("3. Exit");
        System.out.print("Enter choice: ");

        int choice = getIntInput(1, 3);

        switch (choice) {
            case 1:
                manageProperties();
                break;
            case 2:
                System.out.println("\nTenant management coming soon!");
                break;
            case 3:
                System.out.println("\nExiting system. Goodbye!");
                System.exit(0);
        }
        showMainMenu();
    }

    private static void manageProperties() {
        System.out.println("\nPROPERTY MANAGEMENT");
        System.out.println("1. Add New Property");
        System.out.println("2. View All Properties");
        System.out.println("3. Search Properties");
        System.out.println("4. Back to Main Menu");
        System.out.print("Enter choice: ");

        int choice = getIntInput(1, 4);

        switch (choice) {
            case 1:
                addProperty();
                break;
            case 2:
                PropertyDAO.displayAllProperties();
                break;
            case 3:
                searchProperties();
                break;
            case 4:
                return;
        }
        manageProperties();
    }

    private static void addProperty() {
        System.out.println("\nADD NEW PROPERTY");
        System.out.print("Address: ");
        String address = scanner.nextLine();

        System.out.print("City: ");
        String city = scanner.nextLine();

        System.out.print("Type (Apartment/House/Villa): ");
        String type = scanner.nextLine();

        System.out.print("Monthly Rent: ");
        double rent = getDoubleInput();

        System.out.print("Status (Available/Rented): ");
        String status = scanner.nextLine();

        if (PropertyDAO.addProperty(new Property(0, address, city, type, rent, status))) {
            System.out.println("\n✓ Property added successfully!");
        } else {
            System.out.println("\n✗ Failed to add property. Please try again.");
        }
    }

    private static void searchProperties() {
        System.out.println("\nSEARCH PROPERTIES");
        System.out.println("1. By City");
        System.out.println("2. By Type");
        System.out.println("3. By Price Range");
        System.out.println("4. Back");
        System.out.print("Enter choice: ");

        int choice = getIntInput(1, 4);

        switch (choice) {
            case 1:
                System.out.print("Enter city name: ");
                PropertyDAO.displayPropertiesByCity(scanner.nextLine());
                break;
            case 2:
                System.out.print("Enter property type: ");
                PropertyDAO.displayPropertiesByType(scanner.nextLine());
                break;
            case 3:
                System.out.print("Enter minimum rent: ");
                double min = getDoubleInput();
                System.out.print("Enter maximum rent: ");
                double max = getDoubleInput();
                PropertyDAO.displayPropertiesByPriceRange(min, max);
                break;
            case 4:
                return;
        }
        searchProperties();
    }

    // Input validation methods
    private static int getIntInput(int min, int max) {
        while (true) {
            try {
                int input = scanner.nextInt();
                scanner.nextLine();
                if (input >= min && input <= max) {
                    return input;
                }
                System.out.print("Please enter between " + min + "-" + max + ": ");
            } catch (Exception e) {
                scanner.nextLine();
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    private static double getDoubleInput() {
        while (true) {
            try {
                double input = scanner.nextDouble();
                scanner.nextLine();
                return input;
            } catch (Exception e) {
                scanner.nextLine();
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
}