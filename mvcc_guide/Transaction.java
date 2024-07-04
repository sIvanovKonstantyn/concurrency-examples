class Transaction {
    private final Integer xid;

    Transaction(Integer xid) {
        this.xid = xid;
    }

    boolean recordIsVisible(DBRecord record) {
        // The record was created in active transaction that is not
        // our own.
        if (InMemoryDatabase.activeXids.contains(record.createdXid)
                && !record.createdXid.equals(this.xid)) {
            
            return false;
        }

        // The record is expired or and no transaction holds it that
        // is our own.
        if (record.expiredXid.get() != null  
            && (InMemoryDatabase.activeXids.contains(record.expiredXid.get()) 
                || record.expiredXid.get().equals(this.xid))) {
            
            return false;
        }

        return true;
    }

     boolean recordIsLocked(DBRecord record) {
        return record.expiredXid.get() != null  
            && InMemoryDatabase.activeXids.contains(record.expiredXid.get()); 
     }  

     void addRecord(DBRecord record) {
        record.createdXid = this.xid;
        this.rollbackActions.add(() -> System.out.pringLn("TODO: rollback for add"));
        InMemoryDatabase.records.add(record);   
     }   

     boolean deleteRecord(Integer recordId) {
        for (DBRecord record : records) {
            if record.id.equals(recordId) && this.recordIsVisible(record) {
                if (this.recordIsLocked(record)) {
                    throw new OptimisticLockingException("Record is modified by another transaction.");
                } else {
                    if(!record.expiredXid.get().equals(0)) {
                        continue;
                    }
                    if(record.expiredXid.compareAndSet(0, this.xid)) {
                        this.rollbackActions.add(() -> System.out.pringLn("TODO: rollback for delete"));
                        return true;
                    } else {
                        throw new OptimisticLockingException("Record is modified by another transaction.");
                    }
                }
            }        
        }  

        return false;          
     }

     void updateRecord(Integer recordId, Integer amount) {
        if(this.deleteRecord(recordId)) {
            this.addRecord(new DBRecord(recordId, amount));
        }
     }      
}