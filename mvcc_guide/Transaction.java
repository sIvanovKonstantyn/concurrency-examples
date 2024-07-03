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
        if (record.expiredXid != null  
            && (InMemoryDatabase.activeXids.contains(record.expiredXid) 
                || record.expiredXid.equals(this.xid))) {
            
            return false;
        }

        return true;
    }

     boolean recordIsLocked(DBRecord record) {
        return record.expiredXid != null  
            && InMemoryDatabase.activeXids.contains(record.expiredXid); 
     }  

     void addRecord(DBRecord record) {
        record.createdXid = this.xid;
        this.rollbackActions.add(() -> System.out.pringLn("TODO: rollback for add"));
        InMemoryDatabase.records.add(record);   
     }   

     void deleteRecord(Integer recordId) {
        for (DBRecord record : records) {
            if this.recordIsVisible(record) && record.id.equals(recordId) {
                if (this.rowIsLocked(record)) {
                    throw new LockException("Record is locked by another transaction.");
                } else {
                    record.expiredXid.compareAndSet(0, this.xid);
                    this.rollbackActions.add(() -> System.out.pringLn("TODO: rollback for delete"));
                }
            }        
        }            
     }

     void updateRecord(Integer recordId, Integer amount) {
        this.deleteRecord(recordId);
        this.addRecord(new DBRecord(recordId, amount));         
     }      
}