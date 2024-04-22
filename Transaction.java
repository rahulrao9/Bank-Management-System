import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Transaction {
    private int accountId;

    public Transaction(int accountId) {
        this.accountId = accountId;
    }

    public void withdraw(int oldBalance, int amount) {
        int newBalance = oldBalance - amount;
        Account account = new Account(accountId);
        account.setBalance(newBalance);
        writeUpdatedBalance(newBalance);
        writeTransactionToFile("withdraw", amount);
    }

    public void deposit(int oldBalance, int amount) {
        int newBalance = oldBalance + amount;
        Account account = new Account(accountId);
        account.setBalance(newBalance);
        writeUpdatedBalance(newBalance);
        writeTransactionToFile("deposit", amount);
    }

    public interface TransferCommand {
        void execute();
    }
    
    public class Transfer implements TransferCommand {
        private int senderAccountId;
        private int receiverAccountId;
        private int amount;
    
        public Transfer(int senderAccountId, int receiverAccountId, int amount) {
            this.senderAccountId = senderAccountId;
            this.receiverAccountId = receiverAccountId;
            this.amount = amount;
        }
    
        @Override
        public void execute() {
            int senderOldBalance = readAccountBalance(senderAccountId);
        if (senderOldBalance == -1) {
            System.out.println("Sender account does not exist. Transfer cancelled.");
            return;
        }
        
        // Read receiver's balance from accounts.txt
        int receiverOldBalance = readAccountBalance(receiverAccountId);
        if (receiverOldBalance == -1) {
            System.out.println("Receiver account does not exist. Transfer cancelled.");
            return;
        }
        
        // Calculate new balances after transfer
        int senderNewBalance = senderOldBalance - amount;
        int receiverNewBalance = receiverOldBalance + amount;
        
        // Check if sender has sufficient funds
        if (senderNewBalance < 0) {
            System.out.println("Insufficient funds for transfer. Transfer cancelled.");
            return;
        }
        
        // Update sender's balance in accounts.txt
        writeUpdatedBalance(senderAccountId, senderNewBalance);
        
        // Update receiver's balance in accounts.txt
        writeUpdatedBalance(receiverAccountId, receiverNewBalance);
        
        // Log the transaction
        writeTransactionToFile("transfer", amount, senderAccountId, receiverAccountId);
        }
    }
    
    public class TransferManager {
        public void executeTransfer(TransferCommand transferCommand) {
            transferCommand.execute();
        }
    }

    public void transfer(int senderAccountId, int receiverAccountId, int amount) {
        // Read sender's balance from accounts.txt
        int senderOldBalance = readAccountBalance(senderAccountId);
        if (senderOldBalance == -1) {
            System.out.println("Sender account does not exist. Transfer cancelled.");
            return;
        }
        
        // Read receiver's balance from accounts.txt
        int receiverOldBalance = readAccountBalance(receiverAccountId);
        if (receiverOldBalance == -1) {
            System.out.println("Receiver account does not exist. Transfer cancelled.");
            return;
        }
        
        // Calculate new balances after transfer
        int senderNewBalance = senderOldBalance - amount;
        int receiverNewBalance = receiverOldBalance + amount;
        
        // Check if sender has sufficient funds
        if (senderNewBalance < 0) {
            System.out.println("Insufficient funds for transfer. Transfer cancelled.");
            return;
        }
        
        // Update sender's balance in accounts.txt
        writeUpdatedBalance(senderAccountId, senderNewBalance);
        
        // Update receiver's balance in accounts.txt
        writeUpdatedBalance(receiverAccountId, receiverNewBalance);
        
        // Log the transaction
        writeTransactionToFile("transfer", amount, senderAccountId, receiverAccountId);
    }

    public void writeUpdatedBalance(int accountId, int newBalance) {
        try (Scanner scanner = new Scanner(new File("accounts.txt"))) {
            List<String> updatedLines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 5 && Integer.parseInt(parts[2]) == accountId) {
                    // Update the balance in the current line
                    parts[4] = String.valueOf(newBalance);
                    line = String.join(",", parts);
                }
                updatedLines.add(line); // Add the line to the list whether it's updated or not
            }
        
            // Write the updated lines back to the file
            try (PrintWriter writer = new PrintWriter(new FileWriter("accounts.txt"))) {
                for (String updatedLine : updatedLines) {
                    writer.println(updatedLine);
                }
            } catch (IOException e) {
                System.err.println("Error writing updated balance to file: " + e.getMessage());
            }
        } catch (FileNotFoundException e) {
            System.err.println("File 'accounts.txt' not found.");
        }
    }
    

    private int readAccountBalance(int accountId) {
        try (Scanner scanner = new Scanner(new File("accounts.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 5 && Integer.parseInt(parts[2]) == accountId) {
                    return Integer.parseInt(parts[4]); // Return the balance if account ID matches
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File 'accounts.txt' not found.");
        }
        return -1; // Return -1 if account not found or file not found
    }


    private void writeUpdatedBalance(int newBalance) {
        try (Scanner scanner = new Scanner(new File("accounts.txt"))) {
            List<String> updatedLines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 5 && Integer.parseInt(parts[2]) == accountId) {
                    // Update the balance in the current line
                    parts[4] = String.valueOf(newBalance);
                    line = String.join(",", parts);
                }
                updatedLines.add(line); // Add the line to the list whether it's updated or not
            }
    
            // Write the updated lines back to the file
            try (PrintWriter writer = new PrintWriter(new FileWriter("accounts.txt"))) {
                for (String updatedLine : updatedLines) {
                    writer.println(updatedLine);
                }
            } catch (IOException e) {
                System.err.println("Error writing updated balance to file: " + e.getMessage());
            }
        } catch (FileNotFoundException e) {
            System.err.println("File 'accounts.txt' not found.");
        }
    }
    
    
    private void writeTransactionToFile(String type, int amount) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("transactions.txt", true))) {
            writer.println( accountId + ","+ type+ "," + amount);
        } catch (IOException e) {
            System.err.println("Error writing transaction to file: " + e.getMessage());
        }
    }

    private void writeTransactionToFile(String type, int amount, int senderAccountId, int receiverAccountId) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("transactions.txt", true))) {
            writer.println(senderAccountId+"," +  type+ 
            "," + receiverAccountId + amount);
        } catch (IOException e) {
            System.err.println("Error writing transaction to file: " + e.getMessage());
        }
    }


}
