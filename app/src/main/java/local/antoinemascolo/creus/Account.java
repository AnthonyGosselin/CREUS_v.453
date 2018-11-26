package local.antoinemascolo.creus;

public class Account {
    private int id;
    private double balance;

    public Account(int id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean pay(double amount){
        if(this.balance - amount >= 0){
            this.balance -= amount;
            return true;
        }else return false;
    }
}
