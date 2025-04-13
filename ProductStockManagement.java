import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class ProductStockManagement {
    static String[][] stocks;
    static int numStocks;
    static int[] catalogSizes;
    static ArrayList<String> insertHistory = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static boolean isStockSetup = false;

    public static void main(String[] args) {
        System.out.println("-".repeat(46));
        System.out.println("[ Welcome to Product Stock Management System ]");
        System.out.println("-".repeat(46));
        Scanner scanner = new Scanner(System.in);
        int choice;
        while (true) {
            System.out.print("""
                    1. Set up Stock.
                    2. View Stock.
                    3. Insert Product to Stock.
                    4. Update Product to Stock.
                    5. Delete Product from Stock.
                    6. View insertion history.
                    7. Exit program...
                    """);
            System.out.print("\nEnter your choice: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    setupStock();
                    break;
                case 2:
                    if (!isStockSetup) {
                        System.out.println("You need to set up stock first (Option 1)!");
                        break;
                    }
                    viewStock();
                    break;
                case 3:
                    if (!isStockSetup) {
                        System.out.println("You need to set up stock first (Option 1)!");
                        break;
                    }
                    insertProduct();
                    break;
                case 4:
                    if (!isStockSetup) {
                        System.out.println("You need to set up stock first (Option 1)!");
                        break;
                    }
                    updateProduct();
                    break;
                case 5:
                    if (!isStockSetup) {
                        System.out.println("You need to set up stock first (Option 1)!");
                        break;
                    }
                    deleteProduct();
                    break;
                case 6:
                    if (!isStockSetup) {
                        System.out.println("You need to set up stock first (Option 1)!");
                        break;
                    }
                    viewInsertHistory();
                    break;
                case 7:
                    System.out.println("Exiting program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    static void setupStock() {
        numStocks = getIntInput("[+] Insert number of Stock: ");
        catalogSizes = new int[numStocks];

        for (int i = 0; i < numStocks; i++) {
            System.out.println("'Insert number of catalogue for each stock.'");
            catalogSizes[i] = getIntInput("[+] Insert number of catalogue on stock [" + (i+1) + "]: ");
        }

        // Create the stocks array with variable catalog sizes
        stocks = new String[numStocks][];
        for (int i = 0; i < numStocks; i++) {
            stocks[i] = new String[catalogSizes[i]];
            // Initialize all slots as "EMPTY"
            for (int j = 0; j < catalogSizes[i]; j++) {
                stocks[i][j] = "EMPTY";
            }
        }

        System.out.println("----- SET UP STOCK SUCCEEDED -----");
        isStockSetup = true;
        viewStock();
    }
    static void viewStock() {
        for (int i = 0; i < numStocks; i++) {
            System.out.print("Stock [" + (i+1) + "] -> ");
            for (int j = 0; j < catalogSizes[i]; j++) {
                System.out.print("[ " + (j+1) + " - " + stocks[i][j] + " ] ");
            }
            System.out.println();
        }
    }
    static void insertProduct() {
        viewStock();

        int stockIndex = getIntInput("[+] Insert stock number: ") - 1;

        if (stockIndex < 0 || stockIndex >= numStocks) {
            System.out.println("Invalid stock number!");
            return;
        }

        // Check if the selected stock has any empty slots
        boolean hasEmptySlot = false;
        for (int j = 0; j < catalogSizes[stockIndex]; j++) {
            if (stocks[stockIndex][j].equals("EMPTY")) {
                hasEmptySlot = true;
                break;
            }
        }

        if (!hasEmptySlot) {
            System.out.println("This stock is full! Cannot insert more products.");
            return;
        }

        int catalogIndex = getIntInput("[+] Insert catalog number: ") - 1;

        if (catalogIndex < 0 || catalogIndex >= catalogSizes[stockIndex]) {
            System.out.println("Invalid catalog number!");
            return;
        }

        if (!stocks[stockIndex][catalogIndex].equals("EMPTY")) {
            System.out.println("This slot is already occupied by: " + stocks[stockIndex][catalogIndex]);
            return;
        }

        System.out.print("[+] Insert product name: ");
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
    static void updateProduct() {
        viewStock();

        int stockIndex = getIntInput("[+] Insert stock number to update: ") - 1;

        if (stockIndex < 0 || stockIndex >= numStocks) {
            System.out.println("Invalid stock number!");
            return;
        }

        int catalogIndex = getIntInput("[+] Insert catalog number to update: ") - 1;

        if (catalogIndex < 0 || catalogIndex >= catalogSizes[stockIndex]) {
            System.out.println("Invalid catalog number!");
            return;
        }

        if (stocks[stockIndex][catalogIndex].equals("EMPTY")) {
            System.out.println("This slot is empty! Nothing to update.");
            return;
        }

        System.out.println("Current product: " + stocks[stockIndex][catalogIndex]);
        System.out.print("[+] Insert new product name: ");
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
    static void deleteProduct() {
        viewStock();

        int stockIndex = getIntInput("[+] Insert stock number to delete from: ") - 1;

        if (stockIndex < 0 || stockIndex >= numStocks) {
            System.out.println("Invalid stock number!");
            return;
        }

        System.out.print("[+] Insert product name to delete: ");
        String productName = scanner.nextLine();

        // Check if there are multiple instances of the product
        ArrayList<Integer> matchingPositions = new ArrayList<>();
        for (int j = 0; j < catalogSizes[stockIndex]; j++) {
            if (!stocks[stockIndex][j].equals("EMPTY") &&
                    stocks[stockIndex][j].toLowerCase().equals(productName.toLowerCase())) {
                matchingPositions.add(j);
            }
        }

        if (matchingPositions.isEmpty()) {
            System.out.println("Product not found in Stock [" + (stockIndex+1) + "]!");
            return;
        }

        // If there's only one instance, delete it directly
        if (matchingPositions.size() == 1) {
            int positionToDelete = matchingPositions.get(0);
            deleteProductFromPosition(stockIndex, positionToDelete);
            return;
        }

        // If there are multiple instances, ask which one to delete
        System.out.println("Multiple instances of '" + productName + "' found in Stock [" + (stockIndex+1) + "]:");
        for (int i = 0; i < matchingPositions.size(); i++) {
            int catalogPos = matchingPositions.get(i);
            System.out.println((i+1) + ". Catalog position " + (catalogPos+1));
        }

        int selectionIndex = getIntInput("[+] Select which one to delete (1-" + matchingPositions.size() + "): ") - 1;

        if (selectionIndex < 0 || selectionIndex >= matchingPositions.size()) {
            System.out.println("Invalid selection!");
            return;
        }

        int positionToDelete = matchingPositions.get(selectionIndex);
        deleteProductFromPosition(stockIndex, positionToDelete);
    }
    static void deleteProductFromPosition(int stockIndex, int catalogIndex) {
        // Add to history
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);
        insertHistory.add(timestamp + " - Deleted: " + stocks[stockIndex][catalogIndex] +
                " from Stock " + (stockIndex+1) + ", Catalog " + (catalogIndex+1));

        stocks[stockIndex][catalogIndex] = "EMPTY";
        System.out.println("Product deleted successfully!");
        viewStock();
    }
    static void viewInsertHistory() {
        System.out.println("\n===== Insert History =====");
        if (insertHistory.isEmpty()) {
            System.out.println("No history available.");
        } else {
            for (String entry : insertHistory) {
                System.out.println(entry);
            }
        }
    }
    static int getIntInput(String prompt) {
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
}
