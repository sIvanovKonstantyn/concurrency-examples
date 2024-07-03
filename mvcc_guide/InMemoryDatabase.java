class InMemoryDatabase {
   private static AtomicInteger currentXid = new AtomicInteger(1);
   private static Set<Integer> activeXids = ConcurrentHashMap.newKeySet();
   private static records = [];
    def new_transaction():
    global next_xid
    next_xid += 1
    active_xids.add(next_xid)
    return Transaction(next_xid) 
}