import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import java.util.List;


public class Main {
    private static JFrame frame;
    private static JTextField usernameField;
    private static JPasswordField passwordField;
    private static JLabel infoLabel;
    private static JPanel infoPanel;

    public static void main(String[] args) {
        // Create a bank
        Bank bank = new Bank("OOADBank");

        // Open a branch
        bank.openBranch("Bangalore");

        // Assume branchId is 1 (for the first branch)
        int branchId = 1;

        // Create and set up the GUI frame
        frame = new JFrame("Bank Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create login panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add components to the login panel
        loginPanel.add(new JLabel("Username:"), gbc);
        gbc.gridy++;
        loginPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        usernameField = new JTextField(15);
        loginPanel.add(usernameField, gbc);
        gbc.gridy++;
        passwordField = new JPasswordField(15);
        loginPanel.add(passwordField, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        JButton loginButton = new JButton("Login");
        loginPanel.add(loginButton, gbc);

        // Add login panel to the frame
        frame.add(loginPanel, BorderLayout.NORTH);

        // Create info panel
        infoPanel = new JPanel(new BorderLayout());
        infoLabel = new JLabel("Account Information");
        infoPanel.add(infoLabel, BorderLayout.CENTER);

        // Add info panel to the frame
        frame.add(infoPanel, BorderLayout.CENTER);

        // Pack and display the GUI
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);

        // Add action listener to the login button
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                Branch branch = findBranchById(bank, branchId);
                if (branch != null) {
                    Account account = findAccountByUsername(username, password);
                    if (account != null) {
                        displayAccountInfo(account);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Account not found or invalid credentials.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Branch with ID " + branchId + " not found.");
                }
            }
        });
    }

    // Helper method to find a branch by ID
    private static Branch findBranchById(Bank bank, int branchId) {
        for (Branch branch : bank.getBranches()) {
            if (branch.getBranchId() == branchId) {
                return branch;
            }
        }
        return null;
    }

    // Helper method to find an account by username and password within a branch
    private static Account findAccountByUsername(String username, String password) {
        try (Scanner scanner = new Scanner(new File("accounts.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    // Username and password match, return an Account object
                    int accountId = Integer.parseInt(parts[2]);
                    int ownerId = Integer.parseInt(parts[3]);
                    int balance = Integer.parseInt(parts[4]);
                    return new Account(accountId, balance, ownerId, username, password);
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(frame, "File 'accounts.txt' not found.");
        }
        return null;
    }

    // Display account information on the GUI
    // Display account information on the GUI
    // Display account information on the GUI
    private static void displayAccountInfo(Account account) {
        JPanel accountPanel = new JPanel(new BorderLayout());
        Customer customer = account.findCustomerByAccountId(account.getAccountId());
    
        // Create account info label
        StringBuilder info = new StringBuilder("<html>");
        info.append("Account ID: ").append(account.getAccountId()).append("<br>");
        info.append("Owner ID: ").append(account.getOwnerId()).append("<br>");
        info.append("Balance: ₹").append(account.getBalance()).append("<br>");
        // info.append("Username: ").append(account.getUsername()).append("<br>");
        // info.append("Password: ").append(account.getPassword()).append("<br>");
        info.append("</html>");
        JLabel accountInfoLabel = new JLabel(info.toString());
        accountPanel.add(accountInfoLabel, BorderLayout.CENTER);
        
        // Create buttons for deposit, withdraw, transfer, and update password
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton transferButton = new JButton("Transfer");
        JButton updatePasswordButton = new JButton("Update Password");
        JButton loanRequestButton = new JButton("Request Loan");
        JButton editInfoButton = new JButton("Edit Info");
        JButton transactionHistoryButton = new JButton("Transaction History");



        editInfoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newName = JOptionPane.showInputDialog(frame, "Enter new name:");
                String newAddress = JOptionPane.showInputDialog(frame, "Enter new address:");
                if (newName != null && newAddress != null) {
                    customer.editInfo(newName, newAddress);
                    displayAccountInfo(account); // Refresh account information panel
                }
            }
        });
        

    
        // Add action listeners to deposit, withdraw, transfer, and update password buttons
        depositButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String depositAmountString = JOptionPane.showInputDialog(frame, "Enter deposit amount:");
                try {
                    int depositAmount = Integer.parseInt(depositAmountString);
                    if (depositAmount > 0) {
                        account.makeTransaction("deposit", depositAmount);
                        displayAccountInfo(account); // Refresh account information panel
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid deposit amount.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid number.");
                }
            }
        });
    
        withdrawButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String withdrawAmountString = JOptionPane.showInputDialog(frame, "Enter withdraw amount:");
                try {
                    int withdrawAmount = Integer.parseInt(withdrawAmountString);
                    if (withdrawAmount > 0 && withdrawAmount <= account.getBalance()) {
                        account.makeTransaction("withdraw", withdrawAmount);
                        displayAccountInfo(account); // Refresh account information panel
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid withdraw amount.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid number.");
                }
            }
        });
    
        transferButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String receiverAccountIdString = JOptionPane.showInputDialog(frame, "Enter receiver account ID:");
                String transferAmountString = JOptionPane.showInputDialog(frame, "Enter transfer amount:");
                try {
                    int receiverAccountId = Integer.parseInt(receiverAccountIdString);
                    int transferAmount = Integer.parseInt(transferAmountString);
                    if (receiverAccountId > 0 && transferAmount > 0 && transferAmount <= account.getBalance()) {
                        account.makeTransaction("transfer", transferAmount, receiverAccountId);
                        displayAccountInfo(account); // Refresh account information panel
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid transfer.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid numbers.");
                }
            }
        });
    
        updatePasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newPassword = JOptionPane.showInputDialog(frame, "Enter new password:");
                if (newPassword != null && !newPassword.isEmpty()) {
                    String oldPassword = account.getPassword(); // Get the old password from the account
                    account.updatePassword(newPassword, oldPassword); // Call updatePassword with both old and new passwords
                    // Refresh account information panel
                    displayAccountInfo(account);
                }
            }
        });

        loanRequestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String loanAmountString = JOptionPane.showInputDialog(frame, "Enter loan amount:");
                String durationString = JOptionPane.showInputDialog(frame, "Enter loan duration (in years):");
                
                try {
                    int loanAmount = Integer.parseInt(loanAmountString);
                    int duration = Integer.parseInt(durationString);
                    // Check if loan request is valid (e.g., loan amount is positive)
                    if (loanAmount > 0 && duration > 0) {
                        // Check if the loan amount is within the allowed limit (10 times the current balance)
                        int maxLoanAmount = account.getBalance() * 10;
                        if (loanAmount <= maxLoanAmount) {
                            // Create a LoanAccount object with the provided details
                            LoanAccount loanAccount = new LoanAccount(account.getAccountId(), account.getBalance(), account.getOwnerId(), loanAmount, duration, 5);
                            // Loan request is valid, display loan granted message
                            JOptionPane.showMessageDialog(frame, "Loan Granted");
                            float interest = loanAccount.calculateInterest();
                             // Display the interest payable
                            JOptionPane.showMessageDialog(frame, "Interest Payable: ₹" + interest);
                        } else {
                            // Loan request amount exceeds the allowed limit, display denial message
                            JOptionPane.showMessageDialog(frame, "Loan request denied. Maximum loan amount allowed: ₹" + maxLoanAmount);
                        }
                    } else {
                        // Invalid loan request, display error message
                        JOptionPane.showMessageDialog(frame, "Invalid loan request.");
                    }
                } catch (NumberFormatException ex) {
                    // Invalid input, display error message
                    JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid numbers.");
                }
            }
        });
        

        transactionHistoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Call a method to retrieve and display transaction history
                displayTransactionHistory(account);
            }
        });

        
        
    
        // Add buttons to the account panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(transferButton);
        buttonPanel.add(updatePasswordButton);
        buttonPanel.add(loanRequestButton);
        buttonPanel.add(editInfoButton);
        buttonPanel.add(transactionHistoryButton);
        accountPanel.add(buttonPanel, BorderLayout.SOUTH);
    

        
        // Update info panel
        infoPanel.removeAll();
        infoPanel.add(accountPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }
    private static void displayTransactionHistory(Account account) {
        // Retrieve transaction history for the account (you need to implement this method)
        Customer customer = account.findCustomerByAccountId(account.getAccountId());
        List<String> transactionHistory = customer.getTransactionDetails(account.getAccountId());
        
        // Display transaction history in a dialog
        StringBuilder historyMessage = new StringBuilder("Transaction History:\n");
        for (String transaction : transactionHistory) {
            historyMessage.append(transaction).append("\n");
        }
        JOptionPane.showMessageDialog(frame, historyMessage.toString());
    }


}
   
