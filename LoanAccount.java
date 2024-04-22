public class LoanAccount extends Account {
    private int loanAmount;
    private float rate;
    private int duration;

    public LoanAccount(int accountId, int balance, int ownerId, int loanAmount, int duration, float rate) {
        super(accountId, balance, ownerId, "loan" + accountId, "loan" + accountId); // Since loan accounts don't have a username/password
        this.loanAmount = loanAmount;
        this.rate = rate;
        this.duration = duration;
        Transaction transaction = new Transaction(accountId);
        transaction.writeUpdatedBalance(accountId, balance + loanAmount);
    }

    public float calculateInterest() {
        float simpleInterest = (loanAmount * rate * duration) / 100;
        return simpleInterest;
    }

    public int getLoanAmount(){
        return loanAmount;
    }

    public float getRate(){
        return rate;
    }

}
