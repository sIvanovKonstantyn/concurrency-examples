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

    // (3)
    // The third idea - is an upgrate of (2). But here we use ReadWriteLock iterface. The
    // behaviour here is pretty much the same as in (2) but if we would add the
    // "Get account balance" feature - we'll be able to use read lock there. 
    // It's similar to "exclusive row lock" for write operations and "shared row lock"
    // for read operations in RBDMS
    public void transferMoneyBlocking3(Account accountA, Account accountB, 
        long amount) {

            ReadWriteLock accountALock = accountA.lock;       

            try {
                if(!accountALock.writeLock().tryLock(20, TimeUnit.SECONDS)) {
                    throw new LockTimeoutException(accountA);
                }

                if(accountA.amount < amount) {
                    throw new NegativeBalanceException(accountA, amount);
                }

                accountA.amount -= amount;
                accountB.amount += amount;                 

            } finally {
                accountALock.writeLock().unlock();
            }
    }

    // (4)
    // Non-blocking implementatation. We use atomic integer for amount value.
    // We get its value, check the balance and update the balance only if it's the same as before
    // Similar to optimistic locking in RBDMS but only for a single field.
    // To implement it for multiple fields (smth like non-blocking update of account) using the "version" field
    // we have to implement "transaction isolation" first.
    public void transferMoneyBlocking3(Account accountA, Account accountB, 
        long amount) {

            Integer accountABalance = accountA.atomicAmount.get();       

            if(accountABalance < amount) {
                throw new NegativeBalanceException(accountA, amount);
            }

            if(!accountA.atomicAmount.compareAndSet(accountABalance, accountABalance - amount) {
                throw new OptimisticLockException(accountA);
            }

            accountB.atomicAmount.addAndGet(amount);
    }
}