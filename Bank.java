import java.util.*;

class Bank {
    private String name;
    private List<Branch> branches;

    public Bank(String name) {
        this.name = name;
        this.branches = new ArrayList<>();
    }

    public void openBranch(String address) {
        int branchId = branches.size() + 1;
        Branch branch = new Branch(this.name, branchId, address);
        branches.add(branch);
    }

    public void closeBranch(int branchId) {
        branches.removeIf(branch -> branch.getBranchId() == branchId);
    }
    
    public List<Branch> getBranches() {
        return branches;
    }
}