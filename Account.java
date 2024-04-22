import java.io.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Account {
    private int accountId;
    private int balance;
    private int ownerId;
    private List<Transaction> transactions;
    private String username;
    private String password;

    public Account(int accountId){
        this.accountId = accountId;
    }

    private List getTransactions(){
        return transactions;
    }

    public Account(int accountId, int balance, int ownerId, String username, String password) 
    {
        this.accountId = accountId;
        this.balance = balance;
        this.ownerId = ownerId;
        this.transactions = new ArrayList<>();
        this.username = username;
        this.password = password;
        System.out.println("Logged in successfully");
        saveAccountDetails();
    }

    protected void saveAccountDetails() {
        // Check if the account details already exist in the file
        if (!accountExists()) {
            try (FileWriter writer = new FileWriter("accounts.txt", true);
                 BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
                String accountDetails = username + "," + password + "," + accountId + "," + ownerId + "," + balance;
                bufferedWriter.write(accountDetails);
                bufferedWriter.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private boolean accountExists() {
        try (Scanner scanner = new Scanner(new File("accounts.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    // Account with the same username and password already exists
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            // File does not exist, so account does not exist either
            return false;
        }
        return false;
    }

    public String getUsername(){
            return username;
    }

    public String getPassword(){
        return password;
    }


    public void updatePassword(String newPassword, String oldPassword) {
        // Read the accounts file and store lines in a list
        List<String> lines = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(new File("accounts.txt"))) {
            while (fileScanner.hasNextLine()) {
                lines.add(fileScanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.err.println("File 'accounts.txt' not found.");
            return;
        }   
    // Find the user's line and update the password
    boolean userFound = false;
    for (int i = 0; i < lines.size(); i++) {
        String[] parts = lines.get(i).split(",");
        if (parts.length >= 5 && parts[0].equals(username) && parts[1].equals(oldPassword)) {
            lines.set(i, username + "," + newPassword + "," + parts[2] + "," + parts[3] + "," + parts[4]); // Update password
            userFound = true;
            break;
        }
    }

    if (userFound) {
        // Write the updated lines back to the file
        try (PrintWriter writer = new PrintWriter(new FileWriter("accounts.txt"))) {
            for (String line : lines) {
                writer.println(line);
            }
            System.out.println("Password updated successfully");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    } else {
        System.out.println("Access denied: old password does not match.");
    }
}

    
public Customer findCustomerByAccountId(int accountId) {
    try (BufferedReader br = new BufferedReader(new FileReader("Customerdetails.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                int accId = Integer.parseInt(parts[1]);
                if (accId == accountId) {
                    int customerId = Integer.parseInt(parts[0]);
                    String name = parts[2];
                    String address = parts[3];
                    return new Customer(customerId, name, address);
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
}

public void makeTransaction(String type, int amount) {
    Transaction transaction = new Transaction(this.accountId);
    switch (type) {
        case "deposit":
            transaction.deposit(this.balance, amount);
            break;
        case "withdraw":
            transaction.withdraw(this.balance, amount);
            break;
        default:
            System.out.println("Invalid transaction type.");
        }
    }

    public void makeTransaction(String type, int amount, int receiverAccountId) {
        Transaction transaction = new Transaction(this.accountId);
        switch (type) {
            case "transfer":
                transaction.transfer(this.accountId, receiverAccountId, amount);
                break;
            default:
                System.out.println("Invalid transaction type.");
        }
    }

    public void setBalance(int amount){
        this.balance = amount;
    }

    public int getAccountId(){
        return accountId;
    }

    public int getBalance(){
        return balance;
    }
    public int getOwnerId(){
        return ownerId;
    }

    public void getAccountDetails(){
        System.out.println("AccountId " + accountId);
        System.out.println("Balance " + balance);
        System.out.println("OwnerId " + ownerId);
        System.out.println("Username " + username);
        System.out.println("Password " + password);
        
    }

}

