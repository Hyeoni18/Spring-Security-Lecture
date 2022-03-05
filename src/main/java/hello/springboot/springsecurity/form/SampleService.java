package hello.springboot.springsecurity.form;

import hello.springboot.springsecurity.common.SecurityLogger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SampleService {

    public void dashboard() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println("==============================");
        System.out.println(userDetails.getUsername());
    }

    @Async //별도의 쓰레드를 만들어 비동기 호출을 함. 근데 이걸 추가한다고 끝나는 게 아님. main class에 @EnableAsync를 추가해야 함.
    public void asyncService() {
        SecurityLogger.log("Async Service");
        System.out.println("Async service is called");
    }
}
