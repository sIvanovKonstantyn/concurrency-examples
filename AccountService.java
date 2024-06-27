class AccountService {

    // (1)
    //The most straighforward idea is to make the entire money transfer method 
    // synchronized. We have to be sure, that it's only one method that manipulates the
    // accounts amount values. In another way - we may face the situation, when 
    // the data will be inconsistent. Since we synchronize all the transaction independetly
    // from the account objects - it's similar to "exclusive table lock" in RDBMS
    public synchronized void transferMoneyBlocking1(Account accountA, Account accountB, 
        long amount) {

            if(accountA.amount >= amount) {
                accountA.amount -= amount;
                accountB.amount += amount;
            }
    }

    // (2)
    // The second idea - make a synchonization on accountA object. Here we only block a
    // specific account and make operations on it. It's similar to "exclusive row lock" in RBDMS
    // Has the same issues - potentially can modify amount values outside.
    public void transferMoneyBlocking2(Account accountA, Account accountB, 
        long amount) {

            synchronized(accountA) {
              if(accountA.amount >= amount) {
                accountA.amount -= amount;
                accountB.amount += amount;
              }  
            }
    }
}