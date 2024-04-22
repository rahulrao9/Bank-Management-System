import java.io.*;
import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


public class Customer {
    private int customerId;
    private List<Account> accounts;
    private String name;
    private String address;

    public Customer(int customerId, String name, String address) {
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.accounts = new ArrayList<>();
    }

    public List<String> getTransactionDetails(int accountId) {
        List<String> transactionDetails = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("transactions.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                int currentAccountId = Integer.parseInt(parts[0]);
                String transactionType = parts[1];
                int amount = Integer.parseInt(parts[2]);
    
                if (transactionType.equals("deposit") || transactionType.equals("withdraw")) {
                    if (currentAccountId == accountId) {
                        transactionDetails.add("Amount $" + amount + " " + transactionType);
                    }
                } else if (transactionType.equals("transfer")) {
                    int receiverAccountId = Integer.parseInt(parts[3]);
                    if (currentAccountId == accountId) {
                        transactionDetails.add("Sent $" + amount + " to account ID " + receiverAccountId);
                    } else if (receiverAccountId == accountId) {
                        transactionDetails.add("Received $" + amount + " from account ID " + currentAccountId);
                    }
                }
            }
        } catch (FileNotFoundException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
            e.printStackTrace();
        }
        return transactionDetails;
    }
    

    
    
    
    
    
    
    
    
    
    

    public void editInfo(String newName, String newAddress) {
        this.name = newName;
        this.address = newAddress;
        updateDetailsInFile(newName, newAddress);
    }

    private void updateDetailsInFile(String newName, String newAddress) {
        String filename = "customerdetails.txt";
        File inputFile = new File(filename);
        File tempFile = new File("temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && Integer.parseInt(parts[0]) == customerId) {
                    writer.write(customerId + "," + parts[1] + "," + newName + "," + newAddress);
                    writer.newLine();
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error updating customer details in file: " + e.getMessage());
            return;
        }

        // Rename the temporary file to the original file
        try {
            // Rename the temporary file to the input file
            Files.move(tempFile.toPath(), inputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            // Handle the error
            System.err.println("Error updating customer details: " + e.getMessage());
        }
    }
}