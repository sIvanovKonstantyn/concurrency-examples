class Account {
  String name;
  long amount;
  ReadWriteLock lock = new ReentrantReadWriteLock();
}
