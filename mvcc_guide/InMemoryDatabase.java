class InMemoryDatabase {
   private static AtomicInteger currentXid = new AtomicInteger(1);
   public static Set<Integer> activeXids = ConcurrentHashMap.newKeySet();
   public static Set<DBRecord> records = ConcurrentHashMap.newKeySet();
   public static Set<Function> rollbackActions = ConcurrentHashMap.newKeySet();

   public static Transaction createNewTransaction() {
        Integer newXid = currentXid.getAndIncrement();
        activeXids.add(newXid);
        return new Transaction(newXid);
   }   
}