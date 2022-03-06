package hello.springboot.springsecurity.form;

import hello.springboot.springsecurity.common.SecurityLogger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import java.util.Collection;

@Service
public class SampleService {

    @Secured("ROLE_USER")
    //@RolesAllowed("ROLE_USER")
 //   @PreAuthorize("hasRole('USER')")
 //   @PostAuthorize() //메소드 실행 이후에 리턴값이 있으면 추가적인 인증을 해줌.
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
