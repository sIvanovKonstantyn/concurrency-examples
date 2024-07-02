class VersionedAccounts {
  static List<Account> accountVersions = new LinkedList<>();
  static AtomicInteger version = new AtomicInteger();
  
  public static Account findByName(String name, Integer version) {
    accountVersions.stream()
      .filter(a -> a.name.equals(name))
      .filter(a -> !a.removed().get())
      .filter(a -> a.version.get() <= version)
      .sorted((a,b)) -> a.version.get().comprateTo(b.version.get()))
      .findFirst()
      .orElseThrow();
  }

  public static Integer currentVersion() {
    return version.getAndIncrement();
  }

  public static boolean deleteAccountVersion(Account account, Integer accountVersion) {
    accountVersions.stream()
      .filter(a -> a.name.equals(account.name))
      .filter(a -> !a.removed().get())
      .filter(a -> a.version.get().equals(accountVersion)
      .forEach(a -> a.removed.set(true));
  }

  public static boolean createAccountVersion(Account account) {
    //constraints checking...

    accountVersions.add(account);

  }

  public static boolean updateAccountVersion(Account account, Integer oldVersion) {
    //constraints checking...
      Account checkAccount = accountVersions.stream()
        .filter(a -> a.name.equals(name))
        .filter(a -> !a.removed().get())
        .filter(a -> a.version.get() <= account.version.get())
        .sorted((a,b)) -> a.version.get().comprateTo(b.version.get()))
        .findFirst()
        .orElseThrow();

      if(!checkAccount.version.equals(oldVersion)) {
          throw new OptimisticLockException(account);
      }

    accountVersions.add(account);
    deleteAccountVersion(oldVersion);
  }
}
