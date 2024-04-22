import java.util.*;

public class Branch {
    private String bankName;
    private int branchId;
    private String address;
    private List<Account> accounts;

    public Branch(String bankName, int branchId, String address) {
        this.bankName = bankName;
        this.branchId = branchId;
        this.address = address;
        this.accounts = new ArrayList<>();
    }

    public List<Account> getAccounts() {
        List<Account> branchAccounts = new ArrayList<>();
        for (Account account : accounts) {
            branchAccounts.add(account);
        }
        return branchAccounts;
    }

    public void openAccount(int accountId, int balance, int ownerId, String username, String password) {
        Account account = new Account(accountId, balance, ownerId, username, password);
        this.accounts.add(account);
    }

    public void closeAccount(int accountId) {
        accounts.removeIf(account -> account.getAccountId() == accountId);
    }

    public int getBranchId(){
        return this.branchId;
    }

    public void getBranchDetails(){
        System.out.println("Bank Name " + bankName);
        System.out.println("Address " + address);
        System.out.println("Branch Id " + branchId);
    }

}
