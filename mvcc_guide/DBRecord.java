class DBRecord {
    Integer createdXid;
    Integer id;
    Integer amount;
    AtomicInteger expiredXid = new AtomicInteger();

    DBRecord(Integer recordId, Integer amount) {
        this.id = recordId;
        this.amount = amount;
    }
}