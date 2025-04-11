

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private String[][] stocks;
    private int numStocks;
    private int catalogSize;
    private ArrayList<String> insertHistory;
    private Scanner scanner;

    public Main() {
        scanner = new Scanner(System.in);
        insertHistory = new ArrayList<>();
    }

    public void start() {
        int choice;
        setupStock();

        do {
            displayMenu();
            choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    setupStock();
                    break;
                case 2:
                    viewStock();
                    break;
                case 3:
                    insertProduct();
                    break;
                case 4:
                    updateProduct();
                    break;
                case 5:
                    deleteProduct();
                    break;
                case 6:
                    viewInsertHistory();
                    break;
                case 7:
                    System.out.println("Exiting program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 7);

        scanner.close();
    }

    private void displayMenu() {
        System.out.println("\n===== Stock Management System =====");
        System.out.println("1. Set up stock");
        System.out.println("2. View stock");
        System.out.println("3. Insert product to stock");
        System.out.println("4. Update product in stock");
        System.out.println("5. Delete product from stock");
        System.out.println("6. View insert history");
        System.out.println("7. Exit");
        System.out.println("===================================");
    }

    private void setupStock() {
        numStocks = getIntInput("Enter number of stocks: ");
        catalogSize = getIntInput("Enter number of catalog slots per stock: ");

        stocks = new String[numStocks][catalogSize];

        // Initialize all slots as "Empty"
        for (int i = 0; i < numStocks; i++) {
            for (int j = 0; j < catalogSize; j++) {
                stocks[i][j] = "Empty";
            }
        }

        System.out.println("Stock setup completed successfully!");
        viewStock();
    }

    private void viewStock() {
        System.out.println("\n===== Current Stock Status =====");

        for (int i = 0; i < numStocks; i++) {
            boolean rowHasEmptySlot = false;

            // Check if the row has at least one empty slot
            for (int j = 0; j < catalogSize; j++) {
                if (stocks[i][j].equals("Empty")) {
                    rowHasEmptySlot = true;
                    break;
                }
            }

            // Display the row only if it has at least one empty slot
            if (rowHasEmptySlot) {
                System.out.print("Stock [" + (i+1) + "] => ");
                for (int j = 0; j < catalogSize; j++) {
                    System.out.print("[ " + (j+1) + " - " + stocks[i][j] + " ] ");
                }
                System.out.println();
            }
        }
    }

    private void insertProduct() {
        viewStock();

        int stockIndex = getIntInput("Enter stock number: ") - 1;

        if (stockIndex < 0 || stockIndex >= numStocks) {
            System.out.println("Invalid stock number!");
            return;
        }

        // Check if the selected stock has any empty slots
        boolean hasEmptySlot = false;
        for (int j = 0; j < catalogSize; j++) {
            if (stocks[stockIndex][j].equals("Empty")) {
                hasEmptySlot = true;
                break;
            }
        }

        if (!hasEmptySlot) {
            System.out.println("This stock is full! Cannot insert more products.");
            return;
        }

        int catalogIndex = getIntInput("Enter catalog number: ") - 1;

        if (catalogIndex < 0 || catalogIndex >= catalogSize) {
            System.out.println("Invalid catalog number!");
            return;
        }

        if (!stocks[stockIndex][catalogIndex].equals("Empty")) {
            System.out.println("This slot is already occupied by: " + stocks[stockIndex][catalogIndex]);
            return;
        }

        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();

        stocks[stockIndex][catalogIndex] = productName;

        // Add to history
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);
        insertHistory.add(timestamp + " - Inserted: " + productName + " to Stock " + (stockIndex+1) + ", Catalog " + (catalogIndex+1));

        System.out.println("Product successfully inserted!");
        viewStock();
    }

    private void updateProduct() {
        viewStock();

        int stockIndex = getIntInput("Enter stock number to update: ") - 1;

        if (stockIndex < 0 || stockIndex >= numStocks) {
            System.out.println("Invalid stock number!");
            return;
        }

        int catalogIndex = getIntInput("Enter catalog number to update: ") - 1;

        if (catalogIndex < 0 || catalogIndex >= catalogSize) {
            System.out.println("Invalid catalog number!");
            return;
        }

        if (stocks[stockIndex][catalogIndex].equals("Empty")) {
            System.out.println("This slot is empty! Nothing to update.");
            return;
        }

        System.out.println("Current product: " + stocks[stockIndex][catalogIndex]);
        System.out.print("Enter new product name: ");
        String newProductName = scanner.nextLine();

        String oldProductName = stocks[stockIndex][catalogIndex];
        stocks[stockIndex][catalogIndex] = newProductName;

        // Add to history
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);
        insertHistory.add(timestamp + " - Updated: " + oldProductName + " to " + newProductName +
                " in Stock " + (stockIndex+1) + ", Catalog " + (catalogIndex+1));

        System.out.println("Product successfully updated!");
        viewStock();
    }

    private void deleteProduct() {
        viewStock();
        System.out.print("Enter product name to delete: ");
        String productName = scanner.nextLine();

        boolean found = false;

        for (int i = 0; i < numStocks; i++) {
            for (int j = 0; j < catalogSize; j++) {
                if (!stocks[i][j].equals("Empty") &&
                        stocks[i][j].toLowerCase().equals(productName.toLowerCase())) {

                    // Add to history
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String timestamp = now.format(formatter);
                    insertHistory.add(timestamp + " - Deleted: " + stocks[i][j] +
                            " from Stock " + (i+1) + ", Catalog " + (j+1));

                    stocks[i][j] = "Empty";
                    found = true;
                    System.out.println("Product deleted successfully!");
                }
            }
        }

        if (!found) {
            System.out.println("Product not found!");
        } else {
            viewStock();
        }
    }

    private void viewInsertHistory() {
        System.out.println("\n===== Insert History =====");
        if (insertHistory.isEmpty()) {
            System.out.println("No history available.");
        } else {
            for (String entry : insertHistory) {
                System.out.println(entry);
            }
        }
    }

    private int getIntInput(String prompt) {
        int input = 0;
        boolean valid = false;

        while (!valid) {
            try {
                System.out.print(prompt);
                input = Integer.parseInt(scanner.nextLine());
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }

        return input;
    }

    public static void main(String[] args) {
        Main system = new Main();
        system.start();
    }
}