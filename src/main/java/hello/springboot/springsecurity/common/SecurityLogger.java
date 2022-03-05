package hello.springboot.springsecurity.common;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityLogger {

    public static void log(String message) {
        System.out.println(message); //위치정보
        Thread thread = Thread.currentThread();
        System.out.println("THREAD : "+thread.getName());
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("PRINCIPAL : "+principal);
    }
}
