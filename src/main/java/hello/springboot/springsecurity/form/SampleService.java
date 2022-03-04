package hello.springboot.springsecurity.form;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SampleService {

    public void dashboard() {
        //여기서 SecuuryContext에 들어있는 현재 로그인한 정보(Principal)을 참조하고 싶다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();//로그인한 사용자의 권한을 나타냄.
        Object credentials = authentication.getCredentials();//인증 후에는 가지고 있을 필요 없긴 해. null값이야.
        boolean authenticated = authentication.isAuthenticated();//이게 인증된 사용자냐 판단
    }
}
