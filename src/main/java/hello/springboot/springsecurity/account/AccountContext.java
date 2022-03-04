package hello.springboot.springsecurity.account;

public class AccountContext {

    //쓰레드 로컬 하나 생성
    private static final ThreadLocal<Account> ACCOUNT_THREAD_LOCAL
            = new ThreadLocal<>();

    //Account를 받아 만든 쓰레드 로컬에 넣어둘거야.
    public static void setAccount(Account account) {
        ACCOUNT_THREAD_LOCAL.set(account);
    }

    //꺼내오는 메소드
    public static Account getAccount() {
        return ACCOUNT_THREAD_LOCAL.get();
    }
}
