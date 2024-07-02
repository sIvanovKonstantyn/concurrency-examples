class Account {
  String name;
  long amount;
  ReadWriteLock lock = new ReentrantReadWriteLock();
  AtomicInteger atomicAmount = new AtomicInteger();
  AtomicInteger version = new AtomicInteger();
  AtomicBoolean removed = new AtomicBoolean();
}
